from pydantic import BaseModel, ConfigDict

class InfoBase(BaseModel):
    content: str
    user_id: int

# used when post, put
class InfoCreate(InfoBase):
    pass

# used when get
class Info(InfoBase):
    # model_config = ConfigDict(from_attributes =True)  # V2
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
# with ConfigDict, which is for Pydantic 2.x but fastapi uses below 2.x
# https://docs.pydantic.dev/latest/concepts/models/#arbitrary-class-instances
# still pydantic.error_wrappers.ValidationError: 5 validation errors for Member
class Member(MemberBase):
    # model_config = ConfigDict(from_attributes =True)  # V2
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

