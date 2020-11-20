FROM busybox

VOLUME /var/lib/mysql

CMD ["bin/true"]


# MYSQL을 도커로 실행

# 이 도커 파일을 통해, docker image build -t example/mysql-data:latest . 와 같이 이미지를 빌드하여
# 볼륨 컨테이너를 만들 수 있다.

# docker container run -d --name mysql-data example/mysql-data:latest 그리고 나서 백그라운드로 실행
# 하지만 CMD 인스트럭션에서 셸을 실행하는 것이 전부기 때문에, 금방 실행이 종료된다.

# 그러고나서 볼륨 컨테이너 mysql-data를 MYSQL 컨테이너에 마운트하여 , MYSQL 컨테이너 실행
# docker container run -d -p 3306:3306 --name mysql -e "MYSQL_ALLOW_EMPTY_PASSWORD=yes" \
# -e "MYSQL_DATABASE=gogs" -e "MYSQL_USER=gogs" -e "MYSQL_PASSWORD=1234" \
# --volumes-from mysql-data mysql:5.7 

# docker container exec -it mysql mysql -u gogs -p gogs 와 같이 mysql에 접속할 수 있다.



# gogs Local Git Repo를 도커로 실행

# docker pull gogs/gogs
# docker volume create --name gogs-data
# docker run --name gogs -p 10022:22 -p 10080:3000 -v gogs-data:/data gogs/gogs
# docker start gogs

# localhost:10080으로 접속하여 gogs 설치, (이때 호스트 주소는 docker.for.mac.localhost:3306과 같이 해야한다)
# 설치 후 localhost:10080/user/login으로 접속하면, gogs Repo를 이용할 수 있다.