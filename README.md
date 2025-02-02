# spring-boot-product-service
Cassandra with Spring Boot, Docker Compose and JWT implementation for securing protected endpoints

Application allows unauthorized users to register or login. 
Register endpoint create User entity in database with random generated UUID and hashed password.
Login endpoint creates toke JWT required for access to products operations. Generated token JWT needs to be sent in Athentication header in format: "Bearer (jwt)".


Build local package
-------------------------------
```
mvn clean package
```


Run application docker-compose:
-------------------------------
 ```
docker-compose up -d
```


Wait for Cassandra start and initialize Cassandra database on docker: 
-------------------------------

 ```
docker exec -it cassandra cqlsh -f /docker-entrypoint-initdb.d/init.cql
```

Curls for register/login
-------------------------------

 ```
 curl -X POST "http://localhost:8080/auth/register" -H "Content-Type: application/json" -d '{"username": "user", "password": "password"}'
```
 ```
 curl -X POST "http://localhost:8080/auth/login" -H "Content-Type: application/json" -d '{"username": "user", "password": "password"}'
```
