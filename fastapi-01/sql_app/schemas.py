from pydantic import BaseModel

class MemberBase(BaseModel):
    name: str
    role: str

# used when post, put
class MemberCreate(MemberBase):
    pass

# used when get
class Member(MemberBase):
    id: int
    class Config:
        orm_mode = True  # V1



