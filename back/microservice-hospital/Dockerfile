FROM eclipse-temurin:17-jdk-alpine as build
RUN mkdir -p /build
WORKDIR /build
COPY . /build
RUN ./gradlew -x test clean build

FROM eclipse-temurin:17-jdk-alpine as RUN
RUN mkdir -p /app
WORKDIR /app
COPY --from=build /build/build/libs/POC-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","/app/app.jar"]