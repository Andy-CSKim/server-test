#from typing import List
from typing import Optional
from sqlalchemy import ForeignKey, Boolean, Integer, String, LargeBinary
from sqlalchemy import String
from sqlalchemy.orm import DeclarativeBase
from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from sqlalchemy.orm import relationship

# model = entity
class Base(DeclarativeBase):
    pass

class Member(Base):
    __tablename__ = "member"
    id = mapped_column(Integer, primary_key=True)
    # id: Mapped[int] = mapped_column(primary_key=True)
    name = mapped_column(String)
    role = mapped_column(String)

    # schema.Member.infos is list[schema.Info]
    infos = relationship("InfoBase", lazy="selectin") # exclude user_id
    # infos = relationship("Info", lazy="selectin")
    # raw_data = relationship("RawData", lazy="selectin")
    # raw_data = relationship("RawData", uselist=False, back_populates="member")  # 1 to 1
    # raw_data = relationship("RawData", uselist=False, lazy='selectin')  # 1 to 1
    raw_data = relationship("RawDataBase", uselist=False, lazy='selectin')  # 1 to 1

    def __repr__(self) -> str:
        return f"Member(id={self.id!r}, name={self.name!r}, role={self.role!r})"

class InfoBase(Base):
    __tablename__ = "info"
    id = mapped_column(Integer, primary_key=True)
    content = mapped_column(String)
    # user_id = mapped_column(Integer, ForeignKey("member.id"))
    # member = relationship("Member", lazy="selectin")  # not loading info when loading member
    #member = relationship("Member", back_populates="info")

    def __repr__(self) -> str:
        return f"Info(id={self.id!r}, content={self.content!r})"

class Info(InfoBase):
    # __tablename__ = "info"
    # id = mapped_column(Integer, primary_key=True)
    # content = mapped_column(String)
    user_id = mapped_column(Integer, ForeignKey("member.id"))
    # member = relationship("Member", lazy="selectin")  # not loading info when loading member
    #member = relationship("Member", back_populates="info")

    def __repr__(self) -> str:
        return f"Info(id={self.id!r}, user_id={self.user_id!r}, content={self.content!r})"
    
class RawDataBase(Base):
    __tablename__ = "raw_data"
    id = mapped_column(Integer, primary_key=True)
    file_type = mapped_column(String)
    # content = mapped_column(LargeBinary, nullable=True)
    # user_id = mapped_column(Integer, ForeignKey("member.id"))
    # member = relationship("Member", lazy="selectin")  # not loading info when loading member
    # member = relationship("Member", back_populates="raw_data")

    def __repr__(self) -> str:
        return f"RawDataBase(id={self.id!r} file_type={self.file_type!r})"
    
class RawData(RawDataBase):
    # __tablename__ = "raw_data"
    # id = mapped_column(Integer, primary_key=True)
    content = mapped_column(LargeBinary, nullable=True)
    user_id = mapped_column(Integer, ForeignKey("member.id"))
    # member = relationship("Member", lazy="selectin")  # not loading info when loading member
    # member = relationship("Member", back_populates="raw_data")

    def __repr__(self) -> str:
        return f"RawData(id={self.id!r}, user_id={self.user_id!r}, content size={len(self.content)!r})"