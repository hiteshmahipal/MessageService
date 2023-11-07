FROM gradle:8.4.0-jdk17 as gradle

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN echo "building project"
RUN gradle clean build -x test

FROM alpine

RUN apk update && \
        apk upgrade && apk --no-cache add dumb-init openjdk17 && \
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk

COPY --from=gradle /home/gradle/src/build/libs/* /app/app.jar

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk \
    PATH=$PATH:/usr/lib/jvm/java-17-openjdk/jre/bin:/usr/lib/jvm/java-17-openjdk/bin

WORKDIR /app

EXPOSE 8080

ENTRYPOINT  ["java", "-jar", "./app.jar"]
