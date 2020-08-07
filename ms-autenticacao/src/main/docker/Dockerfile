FROM openjdk:11
MAINTAINER Alvaro Duarte
RUN mkdir -p /usr/local/imagen_docker
COPY bv-rd-autenticacao-0.0.1-SNAPSHOT.jar /usr/local/imagen_docker
copy run.sh /usr/local/run.sh
RUN chmod +x /usr/local/run.sh
WORKDIR /usr/local/
ENTRYPOINT ./run.sh
EXPOSE 8080