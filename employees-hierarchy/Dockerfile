FROM openjdk:8
EXPOSE 8081
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait
ADD target/employees-hierarchy-0.0.2-SNAPSHOT.jar employees-hierarchy.jar