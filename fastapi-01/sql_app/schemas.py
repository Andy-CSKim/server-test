from pydantic import BaseModel, ConfigDict
from typing import List, Union

# used when reading member
class InfoBase(BaseModel):
    content: str

# used when post, put
class InfoCreate(InfoBase):
    user_id: int  # user_id is not included when reading member

# used when get
class Info(InfoCreate):
    id: int  # id should be here
    # model_config = ConfigDict(from_attributes =True)  # V2
    class Config:
        orm_mode = True  # V1

# used when reading member
class RawDataBase(BaseModel):
    file_type: str
    pass

# used when post, put
class RawDataCreate(RawDataBase):
    content: Union[bytes, None] = None
    user_id: int

# used when get
class RawData(RawDataCreate):
    id: int # id should be here

    class Config:
        orm_mode = True  # V1


class MemberBase(BaseModel):
    name: str
    role: str

# used when post, put
class MemberCreate(MemberBase):
    pass

# used when get
# with ConfigDict, which is for Pydantic 2.x but fastapi uses below 2.x
# https://docs.pydantic.dev/latest/concepts/models/#arbitrary-class-instances
# still pydantic.error_wrappers.ValidationError: 5 validation errors for Member
class Member(MemberBase):
    id: int # id should be here
    # model_config = ConfigDict(from_attributes =True)  # V2
    infos: list[InfoBase] = []
    raw_data : RawDataBase = None # error if not None
    class Config:
        orm_mode = True  # V1

# class InfoBase2(BaseModel):
#     content: dict
#     user_id: int

# # used when post, put
# class InfoCreate2(InfoBase2):
#     pass

