FROM openjdk:17
ADD target/listingservice.jar listingservice.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/listingservice.jar"]