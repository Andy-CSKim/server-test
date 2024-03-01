from pydantic import BaseModel

class InfoBase(BaseModel):
    content: str
    user_id: int

# used when post, put
class InfoCreate(InfoBase):
    pass

# used when get
class Info(InfoBase):
    id: int
    class Config:
        orm_mode = True  # V1

class MemberBase(BaseModel):
    name: str
    role: str

# used when post, put
class MemberCreate(MemberBase):
    pass

# used when get
class Member(MemberBase):
    id: int
    infos: list[Info] = []
    class Config:
        orm_mode = True  # V1

# class InfoBase2(BaseModel):
#     content: dict
#     user_id: int

# # used when post, put
# class InfoCreate2(InfoBase2):
#     pass

