FROM gradle:8.12-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle installDist

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/install/your-app-name/ ./
CMD ["bin/your-app-name"]
