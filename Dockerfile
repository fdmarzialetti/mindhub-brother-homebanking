FROM gradle:7.6-jdk11-alpine

COPY . .

RUN gradle build

EXPOSE 8080

ENTRYPOINT ["java","-jar","build/linbs/HomeBanking-0.0.1-SNAPSHOP.jar"]