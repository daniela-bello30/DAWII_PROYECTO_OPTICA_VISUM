# 1. Usamos una imagen muy ligera con Java 17 (la versión que tienes)
FROM eclipse-temurin:17-jdk-alpine

# 2. Creamos una carpeta dentro del contenedor donde vivirá tu app
WORKDIR /app

# 3. Copiamos el archivo .jar que acabamos de compilar (el BUILD SUCCESS)
COPY target/ms-citas-0.0.1-SNAPSHOT.jar app.jar

# 4. Exponemos el puerto de tu microservicio
EXPOSE 8083

# 5. El comando que ejecutará Docker al encenderse
ENTRYPOINT ["java", "-jar", "app.jar"]