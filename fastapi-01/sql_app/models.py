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

    # schema.Member.infos is list[schema.Info]
    infos = relationship("Info", lazy="selectin")

    def __repr__(self) -> str:
        return f"Member(id={self.id!r}, name={self.name!r}, role={self.role!r})"

class Info(Base):
    __tablename__ = "info"
    id = mapped_column(Integer, primary_key=True)
    content = mapped_column(String)
    user_id = mapped_column(Integer, ForeignKey("member.id"))
    # member = relationship("Member", lazy="selectin")  # not loading info when loading member
    #member = relationship("Member", back_populates="info")

    def __repr__(self) -> str:
        return f"Info(id={self.id!r}, user_id={self.user_id!r}, content={self.content!r})"