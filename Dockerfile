FROM debian

MAINTAINER Julien Vince <julien.vince@gmail.com>

USER root

RUN apt-get update

RUN apt-get install -y \
	wget \
	procps \
	nano \
	default-jdk screen

RUN adduser \
   --system \
   --shell /bin/bash \
   --group \
   --disabled-password \
   --home /home/minecraft \
   minecraft

WORKDIR /home/minecraft/

RUN wget https://launcher.mojang.com/v1/objects/bb2b6b1aefcd70dfd1892149ac3a215f6c636b07/server.jar && \
	mv server.jar minecraft_server.jar && \
	touch eula.txt && echo "eula=true" >> eula.txt

COPY /data/minecraft.sh  /etc/init.d/minecraft
COPY /data/server.properties server.properties

RUN mkdir /home/minecraft/backups && \
	mkdir /home/minecraft/world && \
	chmod +x /etc/init.d/minecraft && \
	chown minecraft:minecraft /etc/init.d/minecraft && \
	chown -R minecraft:minecraft /home/minecraft/
	
RUN update-rc.d minecraft defaults

CMD tail -f /dev/null 

	

