# Użycie obrazu OpenJDK jako bazowego
FROM openjdk:17-jdk-slim

# Ustawienie katalogu roboczego w kontenerze
WORKDIR /app

# Kopiowanie pliku JAR do kontenera
COPY target/springboot-cassandra-demo-1.0.0.jar app.jar

# Ekspozycja portu aplikacji
EXPOSE 8080

# Uruchamianie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]