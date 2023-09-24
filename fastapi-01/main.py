from fastapi import FastAPI

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello World"}

@app.get("/query")
async def items(skip: int = 0, limit: int = 10):
    return {"message": "Items", "skip": skip, "limit": limit}

