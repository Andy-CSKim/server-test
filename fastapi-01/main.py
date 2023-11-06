from pydantic import BaseModel
from typing import AsyncGenerator

from fastapi import FastAPI, Depends
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware

from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine
from sql_app import models, schemas, service
from sql_app.database import SessionLocal, engine

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


# async
@app.on_event("startup")
async def on_startup():
    # Not needed if you setup a migration system like Alembic
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


# CRUD API
@app.get("/users", response_model=list[schemas.Member])
#@app.get("/users")
async def read_user(db: AsyncSession = Depends(get_db)) -> list[schemas.Member]:
    users = await service.read_member(db)
    print("get", users)
    print("type(users), len(users)", type(users), len(users), type(users[0]), users[0])
    return users
    #return [schemas.Member.from_orm(user) for user in users]
    #return [{"id": user.id, "name": user.name, "role": user.role} for user in users]

@app.post("/users", response_model=schemas.Member)
async def create_user(member: schemas.MemberCreate, db: AsyncSession = Depends(get_db)) -> schemas.Member:
    user = await service.create_member(db, member)
    print("post", user)
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


# Mounting static web page
app.mount("/", StaticFiles(directory="static", html=True), name="static")