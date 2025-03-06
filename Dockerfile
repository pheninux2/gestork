# Étape de construction avec une image Maven
FROM maven:3.9.5-amazoncorretto-21 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et le code source
COPY pom.xml .
COPY src ./src

# Construire l'application (en ignorant les tests)
RUN mvn clean package -DskipTests

# Étape de production avec OpenJDK
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR construit depuis l'étape précédente
COPY --from=build /app/target/gestork-0.0.1-SNAPSHOT.jar gestork.jar

# Copier le JAR H2
COPY h2-2.3.232.jar h2.jar

# Exposer les ports pour l'application et H2
EXPOSE 8080
EXPOSE 9092

# Commande pour exécuter le serveur H2 et l'application
CMD ["sh", "-c", "java -cp h2.jar:gestork.jar org.h2.tools.Server -tcp -tcpAllowOthers & java -jar gestork.jar"]