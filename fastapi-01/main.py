from pydantic import BaseModel
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles

app = FastAPI()

class LengthRequestDto(BaseModel):
    value: int
    unit: str


@app.get("/hello")
async def root():
    # dictionary will be converted to json
    return {"message": "Hello World"}

@app.get("/height/{value}")  # path parameter
async def height(value: int):
    print(f"============= height: {value} =============")
    return str(value * 100) + " feet"  # value * 100 -> "10000" + " feet" -> "10000 feet

@app.get("/length")  # query parameter. ?value=100
async def length(value: int):
    print(f"============= length: {value} =============")
    return str(value * 100) + " feet"  # value * 100 -> "10000" + " feet" -> "10000 feet

@app.get("/api/length/{value}")
async def length(value: int, unit: str):
    if unit == "inch":
        return str(value / 2.54) + " inch"
    elif unit == "feet":
        return str(value / 0.3048) + " feet"
    else:
        return "invalid unit"

@app.post("/api/length")
async def length(length_request_dto: LengthRequestDto):
    if length_request_dto.unit == "inch":
        return {"result" : str(length_request_dto.value / 2.54) + " inch" }
    elif length_request_dto.unit == "feet":
        return {"result" : str(length_request_dto.value / 0.3048) + " feet"}
    else:
        return {"result": "invalid unit"}


# Mounting static web page
app.mount("/", StaticFiles(directory="static", html=True), name="satic")