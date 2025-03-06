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

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "gestork.jar"]