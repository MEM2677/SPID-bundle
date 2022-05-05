FROM openjdk:8u332-slim-bullseye

RUN mkdir /spid-provider
COPY spid-provider.jar /spid-provider
COPY spid-provider-installer.jar /spid-provider
WORKDIR /spid-provider
CMD java -jar spid-provider-installer.jar
