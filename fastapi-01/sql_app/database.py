#from sqlalchemy import create_engine
#from sqlalchemy.ext.declarative import declarative_base
#from sqlalchemy.orm import sessionmaker

#async
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine


#SQLALCHEMY_DATABASE_URL = "sqlite:///./sql_app.db"
#SQLALCHEMY_DATABASE_URL = "postgresql://testuser:1234@localhost:5432/postgres"  # default is postgresql+psycopg2://
#async
SQLALCHEMY_DATABASE_URL = "postgresql+asyncpg://testuser:1234@localhost:5550/postgres"  # default is postgresql+psycopg2://


# engine = create_engine(
#     #SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": True}  # sqlite requires False
#     SQLALCHEMY_DATABASE_URL
# )
# SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

#async
engine = create_async_engine(SQLALCHEMY_DATABASE_URL)
SessionLocal = async_sessionmaker(engine, expire_on_commit=False)

#Base = declarative_base()