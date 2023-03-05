FROM openjdk:13-jdk-buster

LABEL maintainer="Kicey"

ADD ./build/install/openTCS-Kernel/ /home/openTCS-Kernel/

WORKDIR /home/openTCS-Kernel
RUN chmod +x ./startKernel.sh
# For debug, use this command
# ENTRYPOINT tail -f /dev/null
ENTRYPOINT ./startKernel.sh

EXPOSE 1099 55100 55200
EXPOSE 55000-55010
