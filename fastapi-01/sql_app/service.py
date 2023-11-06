from sqlalchemy.orm import Session
from sqlalchemy import select, func, text

from sqlalchemy.ext.asyncio import AsyncSession

from sql_app import models, schemas, database

async def create_db_and_tables():
    async with database.engine.begin() as conn:
        await conn.run_sync(models.Base.metadata.create_all)

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
    db_member.name = member_create.name
    db_member.role = member_create.role
    await db.commit()
    await db.refresh(db_member)
    return db_member

async def delete_member(db: AsyncSession, user_id: int) -> schemas.Member:
    db_member = await db.get(models.Member, user_id)
    await db.delete(db_member)
    await db.commit()
    return db_member