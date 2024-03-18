### prepare database  
(1) install docker-desktop
    install volume backup & share extension  
(2) install docker composer  
(3) run "docker-compose up -d" in the root folder of the project  
(4) see if the container,'server-test-db'. is running  
(5) see if there is the volume, 'sever-test-db_data'
(6) import "json-server.tar.zst" into the volume, 'sever-test-db_data' through volume backup & share extension  
(7) see if the data is imported  

### fastapi server  
- cd directory    
poetry install (one time)  

- activate virtual env.  
poetry shell  

- run the server
python main.py  

### spring-boot  
- configure smart tomcat plugin in the IDE with port 3001  
- build the project 
- run the project

### node server
- cd directory
yarn install (one time)

- run prisma (whenever changeing schema.prisma)
yarn prisma generate

- run the server
yarn dev

### react client
- cd directory
yarn install (one time)

- run the client
yarn start
