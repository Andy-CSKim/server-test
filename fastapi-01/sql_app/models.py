#from typing import List
from typing import Optional
from sqlalchemy import ForeignKey, Boolean, Integer, String
from sqlalchemy import String
from sqlalchemy.orm import DeclarativeBase
from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from sqlalchemy.orm import relationship

class Base(DeclarativeBase):
    pass

class Member(Base):
    __tablename__ = "member"
    id = mapped_column(Integer, primary_key=True)
    name = mapped_column(String)
    role = mapped_column(String)

    def __repr__(self) -> str:
        return f"Member(id={self.id!r}, name={self.name!r}, role={self.role!r})"

