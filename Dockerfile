FROM openjdk:18-jdk-slim
COPY "./build/libs/payment-0.0.1-SNAPSHOT.jar" "app.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

#ENV PORT 8080
#./gradlew build
#java -jar build/libs/gs-spring-boot-docker-0.1.0.jar
#docker build -t <name> .
#docker run -e PORT=5003 --name spring-boot-1 -p 5000:5003 spring-boot-docker-test:latest

#Subir a docker hub
#docker login
# docker tag spring-boot-docker-test-2<image> diegoguerreroborda/test (username/dir)
# docker push diegoguerreroborda/test (username/dir)
