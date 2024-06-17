# @Description  : Dockerfile for sanrocks trading bot
# @Author       : Sanjeev Saxena
# @version      : v1.0

FROM openjdk

LABEL author Sanjeev

ENV SERVICE_NAME=trading-bot
ENV SERVICE_HOME=/usr/local/trading-bot
ENV SERVICE_SCRIPTS=${SERVICE_HOME}/scripts
ENV CONFIG=config
ENV LIB=lib

RUN mkdir -p $SERVICE_HOME
RUN mkdir -p $SERVICE_SCRIPTS

WORKDIR $SERVICE_HOME

RUN mkdir -p $LIB
RUN mkdir -p $CONFIG

ADD ./build/distributions/$SERVICE_NAME-1.0.tar $SERVICE_HOME
COPY ./build/scripts $SERVICE_SCRIPTS

RUN pwd
RUN ls -lart

RUN chmod 777 $SERVICE_SCRIPTS/$SERVICE_NAME
RUN ls -lart

EXPOSE 1000
ENTRYPOINT $SERVICE_SCRIPTS/$SERVICE_NAME