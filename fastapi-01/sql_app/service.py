from sqlalchemy.orm import Session
from sqlalchemy import select, func, text

from sqlalchemy.ext.asyncio import AsyncSession

from sql_app import models, schemas, database

# dict to string
import json

async def create_db_and_tables():
    async with database.engine.begin() as conn:
        await conn.run_sync(models.Base.metadata.create_all)

# DB -> models.Member ->schemas.Member
async def read_member(db: AsyncSession) -> list[schemas.Member]:
    query = select(models.Member)
    result = await db.execute(query)

    # https://stackoverflow.com/questions/72790215/sqlalchemy-2-x-with-specific-columns-makes-scalars-return-non-orm-objects
    #return result.all()  # returns Row object
    return result.scalars().all() # returns Member object

async def create_member(db: AsyncSession, member_create: schemas.MemberCreate) -> schemas.Member:
    # member_create has name and role, but id is not assigned yet
    new_member = models.Member(**member_create.dict()) # id will be assigned by DB
    db.add(new_member)
    await db.commit()
    await db.refresh(new_member)
    return new_member

async def update_member(db: AsyncSession, user_id: int, member_create: schemas.MemberCreate) -> schemas.Member:
    db_member = await db.get(models.Member, user_id)

    # if db_member exists, update it
    if db_member:
        db_member.name = member_create.name
        db_member.role = member_create.role
        await db.commit()
        await db.refresh(db_member)
        return db_member
    
    # if db_member does not exist, return error
    return None

async def delete_member(db: AsyncSession, user_id: int) -> str:
    db_member = await db.get(models.Member, user_id)

    # if db_member exists, delete it
    if db_member:
        await db.delete(db_member)
        await db.commit()
        return 'OK'
    
    # if db_member does not exist, return error
    return 'Not found'


# info
async def read_info(db: AsyncSession, user_id: int) -> list[schemas.Info]:
    query = select(models.Info).filter(models.Info.user_id == user_id)
    result = await db.execute(query)
    return result.scalars().all()


async def create_info(db: AsyncSession, info_create: schemas.InfoCreate) -> schemas.Info:
    # info_create has content and user_id, but id is not assigned yet
    print("as string", info_create.content, info_create.user_id)
    new_info = models.Info(**info_create.dict()) # id will be assigned by DB
    db.add(new_info)
    await db.commit()
    await db.refresh(new_info)
    return new_info

async def create_info2(db: AsyncSession, user_id: int, data: dict) -> schemas.Info:
    # info_create has content and user_id, but id is not assigned yet
    print("as object", data)
    # info_create.content = info_create.content.toString()
    # new_info = models.Info(**info) # id will be assigned by DB

    # import json
    # serialized = json.dumps(dict)
    # deserialized = json.loads(serialized)

    new_info = models.Info(content=json.dumps(data), user_id=user_id) # id will be assigned by DB
    db.add(new_info)
    await db.commit()
    await db.refresh(new_info)
    return new_info
    # return info_create


async def update_info(db: AsyncSession, info_id: int, info_create: schemas.InfoCreate) -> schemas.Info:
    db_info = await db.get(models.Info, info_id)

    # if db_info exists, update it
    if db_info:
        db_info.content = info_create.content
        db_info.user_id = info_create.user_id
        await db.commit()
        await db.refresh(db_info)
        return db_info
    
    # if db_info does not exist, return error
    return None

async def delete_info(db: AsyncSession, info_id: int) -> str:
    db_info = await db.get(models.Info, info_id)

    # if db_info exists, delete it
    if db_info:
        await db.delete(db_info)
        await db.commit()
        return 'OK'
    
    # if db_info does not exist, return error
    return 'Not found'


# save raw data
async def save_raw_data(db: AsyncSession, user_id: int, file_type: str, data: bytes) -> schemas.RawData:
    # db_member = await db.get(models.Member, user_id)
    query = select(models.RawData).filter(models.RawData.user_id == user_id)
    result = await db.execute(query)

    result = result.scalars().first()

    # print(f"save_raw_data : query = {result}, user id = {user_id}")
    # if RawData exists, update it
    if result:
        print(f"update raw data : {file_type}")
        new_raw_data = result
        new_raw_data.content = data
        new_raw_data.file_type = file_type
    else:
        print(f"create raw data : {file_type}")
        new_raw_data = models.RawData(content=data, file_type=file_type, user_id=user_id)
        db.add(new_raw_data)

    await db.commit()
    await db.refresh(new_raw_data)
    return new_raw_data
    

# read raw data
async def read_raw_data(db: AsyncSession, user_id: int): # -> bytes:
    query = select(models.RawData).filter(models.RawData.user_id == user_id)
    result = await db.execute(query)

    result = result.scalars().first()    

    return {'file_type': result.file_type, 'content': result.content} if result else None

