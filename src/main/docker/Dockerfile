FROM harborka.qianfan123.com/iwms/hd_jre:811-alpine_tools
LABEL PROJECT="seckill" \
      AUTHOR="janhen"
WORKDIR /opt/app
ENV LC_ALL en_US.UTF-8
ADD docker-entrypoint.sh /opt/app/
RUN chmod +x /opt/app/docker-entrypoint.sh
ADD facility-server-*.jar facility-server.jar
EXPOSE 8080 9004

CMD ["sh", "/opt/app/docker-entrypoint.sh"]