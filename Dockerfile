FROM openjdk:8-jdk-alpine

EXPOSE 4567

RUN apk update
RUN apk add openssl

ADD src /xoliba-ai/src/
ADD gradlew /xoliba-ai/gradlew
ADD build.gradle /xoliba-ai/build.gradle
ADD gradle /xoliba-ai/gradle/

WORKDIR "/xoliba-ai"
RUN ./gradlew build

CMD ./gradlew run
