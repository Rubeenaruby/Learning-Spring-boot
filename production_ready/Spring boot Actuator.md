Spring boot Actuator

Introduction

Spring Boot Actuator is a powerful module that provides a set of built-in endpoints for monitoring and managing Spring Boot 
applications. It helps developers gain insights into the application’s health, metrics, and configurations without writing extensive 
code.

Key features include:

Health Checks: Automatically checks the status of the application and its dependencies (like databases and message brokers) to 
ensure everything is functioning correctly.
Metrics: Collects metrics about the application’s performance, such as memory usage, request counts, and response times, which can
be used for performance tuning.
Environment Information: Exposes details about the application’s environment, including properties and configuration settings.
Custom Endpoints: Allows developers to create custom endpoints tailored to specific needs.
Security: Provides mechanisms to secure the endpoints to prevent unauthorized access.
Actuator is often used in production environments to help maintain and troubleshoot applications effectively.

Installing Spring Boot Actuator
To install Spring Boot Actuator, you can add the following dependency to your pom.xml file:
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
After adding this dependency, reload the maven. And go to this url http://localhost:8080/actuator
<img width="814" height="392" alt="image" src="https://github.com/user-attachments/assets/a4927ac4-d5e6-4508-b47c-ef558b7fbdd9" />

Browser image
You can configure the actuator endpoints in your application.properties or application.yml file. For example, to enable all endpoints,
you can add: management.endpoints.web.exposure.include=*

This will expose all available actuator endpoints, allowing you to access information about your application, such as health checks,
metrics, and environment details. You can then access these endpoints via HTTP, usually at /actuator 
(e.g., http://localhost:8080/actuator/health).
<img width="229" height="118" alt="image" src="https://github.com/user-attachments/assets/5b3c8190-1d4b-4866-9d4b-eac6405ce466" />

Browser image
Remember to secure these endpoints in production to prevent unauthorized access!

Actuator Config
Change the Base Path: You can set a custom base path for all actuator endpoints by adding the following property in your 
application.properties or application.yml: management.endpoints.web.base-path=/management This would make your actuator endpoints
accessible under /management, so for example, the health endpoint would be at /management/health.

Enable or Disable Actuator Endpoints: To control which actuator endpoints are exposed, you can use the following properties:

To enable all actuator endpoints:

management.endpoints.web.exposure.include=*

To enable only specific endpoints (e.g., health, info, beans, env):

management.endpoints.web.exposure.include=health,info,beans,env

Actuator Endpoints

/actuator/beans: Returns a list of all the beans configured in your application, including their details and dependencies.
/actuator/env: Provides information about the Spring Environment properties, such as configuration settings and environment variables.
/actuator/health: Shows the health status of the application and its dependencies (like databases and message queues), 
indicating whether everything is functioning correctly.
/actuator/info: Displays application information, which can be customized through Spring environment properties (e.g., version, 
description).

Custom Configuration

management.endpoints.web.exposure.include=*

management.info.env.enabled=true

info.ABCD=WXYZ

Url

<img width="360" height="212" alt="image" src="https://github.com/user-attachments/assets/c63312c4-d1db-4f9b-bf59-1b4472e12564" />

Output

Browser image

/actuator/mappings: Lists all the @RequestMapping paths in the application, providing an overview of available endpoints and their configurations.
/actuator/threaddump: Provides a thread dump of the application, which can be useful for diagnosing performance issues by examining the state of threads at a given moment.
These endpoints help monitor and manage your application effectively, making it easier to diagnose issues and understand its behavior.

For further guide visit this 

Conclusion

This article emphasizes the significance of Spring Boot Actuator for monitoring and managing applications. It offers built-in 
endpoints for health checks, metrics, and environment details, aiding performance monitoring and troubleshooting. Proper 
configuration and security ensure its effectiveness in production environments.




Online Compilers
DSA Sheets
Mock Tests
Copyright © NGU Education Pvt. Ltd.

Spring boot Actuator | Coding Shuttle
