from pydantic import BaseModel
from typing import AsyncGenerator, Annotated, Union

from contextlib import asynccontextmanager

from fastapi import FastAPI, Depends, File, Form, UploadFile, Response
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware

from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine
from sql_app import models, schemas, service
from sql_app.database import SessionLocal, engine

from io import BytesIO, StringIO

app = FastAPI()

origins = ["http://localhost:3000",
           "http://localhost:3001",
           "http://127.0.0.1:3001",           
            "http://localhost:5173",
           ]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Async
async def get_db() -> AsyncGenerator[AsyncSession, None]:
    async with SessionLocal() as session:
        yield session


# # async
# @app.on_event("startup")
# async def on_startup():
#     # Not needed if you setup a migration system like Alembic
#     await service.create_db_and_tables()

@asynccontextmanager
async def lifespan(app: FastAPI):    
    await service.create_db_and_tables()


# test API
class LengthRequestDto(BaseModel):
    value: int
    unit: str


@app.get("/hello")
async def root():
    # dictionary will be converted to json
    return {"message": "Hello World"}

@app.get("/length/{value}")
async def length(value: int, unit: str):
    if unit == "inch":
        return str(value / 2.54) + " inch"
    elif unit == "feet":
        return str(value / 0.3048) + " feet"
    else:
        return "invalid unit"

@app.post("/length")
async def length(length_request_dto: LengthRequestDto):
    if length_request_dto.unit == "inch":
        return {"result" : str(length_request_dto.value / 2.54) + " inch" }
    elif length_request_dto.unit == "feet":
        return {"result" : str(length_request_dto.value / 0.3048) + " feet"}
    else:
        return {"result": "invalid unit"}

# class is better than dictionary because of type checking
@app.post("/length2")
async def length(length_request_dto: dict):
    if length_request_dto["unit"] == "inch":
        return {"result" : str(length_request_dto["value"] / 2.54) + " inch" }
    elif length_request_dto["unit"] == "feet":
        return {"result" : str(length_request_dto["value"] / 0.3048) + " feet"}
    else:
        return {"result": "invalid unit"}


# CRUD API
# @app.get("/users", response_model=list[schemas.Member]) # invalid dict error if included
@app.get("/users")
# async def read_user(db: AsyncSession = Depends(get_db)) -> list[schemas.Member]:  # invalid dict error if included
async def read_user(db: AsyncSession = Depends(get_db)):  # OK
    users = await service.read_member(db) # --> list[schemas.Member] but returns list[models.Member] actually
    print("get", users)
    print("type(users), len(users)", type(users), len(users), type(users[0]), users[0])
    return users
    #return [schemas.Member.from_orm(user) for user in users]
    #return [{"id": user.id, "name": user.name, "role": user.role} for user in users]

# invalid dict error if changed to list[schemas.MemberBase]
@app.post("/users", response_model=schemas.Member)
async def create_user(member: schemas.MemberCreate, db: AsyncSession = Depends(get_db)) -> schemas.Member:
    user = await service.create_member(db, member)
    print(f"post : type={type(user)}, user={user}")
    return user

@app.put("/users/{user_id}", response_model=schemas.Member)
async def update_user(user_id: int, member: schemas.MemberCreate, db: AsyncSession = Depends(get_db)) -> schemas.Member:
    user = await service.update_member(db, user_id, member)
    print("put", user)
    return user

@app.delete("/users/{user_id}", response_model=str)
async def delete_user(user_id: int, db: AsyncSession = Depends(get_db)) -> str:
    user = await service.delete_member(db, user_id)
    print("delete", user)
    return user

# Info
@app.get("/infos/{user_id}", response_model=list[schemas.Info])
async def read_info(user_id: int, db: AsyncSession = Depends(get_db)) -> list[schemas.Info]:
    infos = await service.read_info(db, user_id)
    print("get", infos)
    return infos

@app.post("/infos", response_model=schemas.Info)
async def create_info(info: schemas.InfoCreate, db: AsyncSession = Depends(get_db)) -> schemas.Info:
    info = await service.create_info(db, info)
    print("infos", info)
    return info

@app.post("/infos2/{user_id}", response_model=schemas.Info)
async def create_info2(user_id: int, data: dict, db: AsyncSession = Depends(get_db)) -> schemas.Info:
    info = await service.create_info2(db, user_id, data)
    print("infos2", info)
    return info



@app.put("/infos/{info_id}", response_model=schemas.Info)
async def update_info(info_id: int, info: schemas.InfoCreate, db: AsyncSession = Depends(get_db)) -> schemas.Info:
    info = await service.update_info(db, info_id, info)
    print("put", info)
    return info

@app.delete("/infos/{info_id}", response_model=str)
async def delete_info(info_id: int, db: AsyncSession = Depends(get_db)) -> str:
    info = await service.delete_info(db, info_id)
    print("delete", info)
    return info


@app.post("/upload_bytes/{user_id}", response_model=str)
# async def upload_bytes(user_id: int, file_type: str = Form(), file: bytes = File(), db: AsyncSession = Depends(get_db)) -> schemas.RawData:
async def upload_bytes(user_id: int, file_type: str, file: bytes = File(), db: AsyncSession = Depends(get_db)) -> str:
    # print("upload_bytes", result)
    # return result
    print(f"upload_bytes : type = {file_type} user id = {user_id}, len = {len(file)}")
    # raw_data = BytesIO(file)
    print("contents", file[:200])

    result = await service.save_raw_data(db, user_id, file_type, file)

    return "OK" if result else "Not OK"

@app.post("/upload_file/{user_id}", response_model=str)
async def upload_file(user_id: int, file_type: str = Form(), file: UploadFile = File(...), db: AsyncSession = Depends(get_db)) -> str:
    # result = await service.upload_file(db, user_id, file)
    # print("upload_file", result)
    # return result

    print(f"upload_file : type = {file_type} user id = {user_id}")
    contents = await file.read()
    print("length of contents", len(contents))
    print("contents", contents[:200])

    result = await service.save_raw_data(db, user_id, file_type, contents)
    #return result

    return "OK" if result else "Not OK"

# return image
@app.get("/download_file/{user_id}", response_model=Union[bytes, None])
async def download(user_id: int, db: AsyncSession = Depends(get_db)) -> Union[bytes, None]:
    result = await service.read_raw_data(db, user_id)
    if result:
        print("download_file", result['file_type']) 
        print(result['content'][:100])
    return Response(result['content'], media_type="image/*" if result['file_type'] == 'image' else "audio/*")

# Mounting static web page
# app.mount("/", StaticFiles(directory="static", html=True), name="static")