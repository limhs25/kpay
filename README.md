# Getting Started


## How it works: 
### Jar Execute 
```
    mvn install -DskipTests
     java -jar target/donation-0.0.1-SNAPSHOT.jar
```
### Docker StandAlone 
```
   docker build -t kpay/donation .
   docker run -p 9001:9001 -t kpay/donation -e "SPRING_PROFILES_ACTIVE=test"
```
### Docker Compose

```
    mvn clean package -Pdocker  
    docker-compose up
```


Appendix A.

**Guide for using endpoint the app:**

Go to [http://localhost:9001/api](http://localhost:9001/api) to test and would specify basic authorization a username: `user` and password: `user` or username: `admin` and password: `admin`
* POST request to `/api/create/` ;
* PUT request to `/api/take/:token` ;
* GET request to `/api/find/:token` ;tomobiles".

or use Swagger API [http://localhost:9001/swagger-ui.html](http://localhost:9001/swagger-ui.html)

and generation API docks [http://localhost:9001/v3/api-docs.yaml](http://localhost:9001/v3/api-docs.yaml)

**3. Docker control commands**
* Check all the images you have:
```bash
docker images
```
* If you have to want see running containers:
```bash
docker ps
```
**4. End stop app**
*  Stop containers:
```bash
docker-compose down
```
* Remove old stopped containers of docker-compose
```bash
docker-compose rm -f
```


## 요구사항 명세
see documentation [카카오페이 공채 server 개발 과제](./카카오페이 공채 server 개발 과제.pdf)