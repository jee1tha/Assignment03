Spring boot application with Thymeleaf to view stock details extracted from an xlsx file downloaded from google drive API.

Build Tool
Maven

Unit Tests
Coverage - 85% classes coverage with JUnit

Java Version 
Java 1.8

Springboot Version
2.1.6.RELEASE

Apache POI version
4.1.1

IDE Used
IntelliJ 

How to use 

Enable google API services - Google drive -> create a service account and provide necessary permission to access your google drive
Place stock record file named "assignment-trade.xlsx" inside google drive folder ( this can be changed from the application.yml )
Place Google service account p12 property file inside the resource folder named “credentialsp12.p12”.
Create directory /var/log/dbs/
Give write and read permission to directory /var/log/dbs/
Build cmd : mvn clean install 
Go to Target folder and run jar by running below command
java - jar target/stockservice-1.0-SNAPSHOT.jar  
Enter the following addresses in the browser to view the respective information,
 localhost:8080/fb - view stock records related to FB
 localhost:8080/google  - view stock records related to google
 localhost:8080/apple - view stock records related to apple



Cron Jobs
Cronjob will run every 3 days to archive records entered 3 days before.


MongoLabs Connection String 

mongodb+srv://root:root@dbs-assignment03-bbags.mongodb.net/test?retryWrites=true&w=majority


Logging

Logfile location - /var/log/dbs/dbs-service.log 	
Log level - info





