(1) install docker-desktop and volume backup & share extension  
(2) install docker composer  
(3) run "docker-compose up -d" in the root folder of the project  
(4) see if the container,'server-test-db'. is running  
(5) import json-server.tar.zst into the volume, 'sever-test-db_data', which the container uses, then run the following  

### fastapi  
- after cloning the repo (one time)  
poetry install  

- run server  
poetry shell  
- in the virtual env.  
uvicorn main:app --reload --port=3001