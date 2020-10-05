FROM openjdk:8
LABEL PROJECT="seckill" \
      AUTHOR="janhen"

WORKDIR /opt/app
ENV LC_ALL en_US.UTF-8
ADD docker-entrypoint.sh /opt/app/
RUN chmod +x /opt/app/docker-entrypoint.sh
ADD seckill-server-*.jar facility-server.jar
EXPOSE 8080 9004

CMD ["sh", "/opt/app/docker-entrypoint.sh"]