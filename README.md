# Managing Data, Masking & Audit project
# Development for Hackathon (December 18, 2021)

![image](https://user-images.githubusercontent.com/58312211/148971778-b8efbc72-43d8-467e-b2bb-a0c94694cb2f.png)

### Stack

Java 11, Spring Framework 5 (MVC, REST, Security), Spring Boot 2, YAML as database, Apache Ignite, Apache Kafka, Gradle / Maven, RegExp, JSON, HTTP Base Authentication, Open API Specification, MapStruct

### Description of program steps

1) Sending a request from the front CRM-BE application
2) Unmasked data in the DMS application (if applicable)
3) Sending a request to the DM application
4) Handling the request in the DM application, work with database (YAML files)
5) Sending a response to the DMS application
6) Masking sensitive data by user role in the DMS application (using Apache Ignite)
7) Writing the results of the request and response in the AW application to a .txt file (using Apache Kafka)
8) Sending summary data to the front CRM-BE application
