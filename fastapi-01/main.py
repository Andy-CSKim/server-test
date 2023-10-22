from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles

app = FastAPI()


@app.get("/hello")
async def root():
    return {"message": "Hello World"}

@app.get("/api/length/{value}")
async def length(value: int, unit: str):
    if unit == "inch":
        return str(value * 2.54) + " inch"
    elif unit == "feet":
        return str(value * 0.3048) + " feet"
    else:
        return "invalid unit"

# Mounting static web page
app.mount("/", StaticFiles(directory="static", html=True), name="satic")