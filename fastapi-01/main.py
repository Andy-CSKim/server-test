from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles

app = FastAPI()


@app.get("/hello")
async def root():
    return {"message": "Hello World"}

@app.get("/query")
async def items(skip: int = 0, limit: int = 10):
    return {"message": "Items", "skip": skip, "limit": limit}

app.mount("/", StaticFiles(directory="static", html=True), name="satic")