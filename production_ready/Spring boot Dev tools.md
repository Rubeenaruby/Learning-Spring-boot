Spring boot Dev tools

Introduction

Spring Boot DevTools is a development toolset designed to enhance the productivity of developers by providing features like 
automatic restart, live reload, and property overrides. It simplifies the process of testing and tweaking applications during 
development by automatically applying changes without requiring a manual restart. DevTools also provides helpful development-time 
features such as disabling caching and enabling detailed debugging, making it easier to iterate quickly and see results immediately. 
It’s ideal for speeding up the feedback loop in local development environments.

Installing DevTools

Add DevTools Dependency:

Make sure you have the spring-boot-devtools dependency in your pom.xml or build.gradle file.
```
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-devtools</artifactId>
</dependency>
```
After this, set the IntelliJ settings to allow restarting.

Ide Setting
In IntelliJ IDEA, you can enable the "Build project automatically" option by going to Settings > Build, Execution, Deployment > Compiler. 
Checking this option allows the IDE to automatically compile your project whenever changes are made, which works well in conjunction 
with Spring Boot DevTools to trigger the automatic restart feature. Make sure to also enable "Allow auto-make to start even if developed 
application is currently running" under the same settings for optimal functionality.
<img width="975" height="732" alt="image" src="https://github.com/user-attachments/assets/dc170081-61bc-48c4-a109-2c25767f31f0" />

Ide settings
After that, go to Advanced Settings and check whether the option 'Allow auto-make to start even if developed application is currently
running' is enabled. If it's not, enable it and then press OK.
Ide advanced settings
Implementations
If we check our DTO class, we can see the ‘id’ attribute is present. After running the application, we can send a request using Postman.
```
package com.example.user.product_ready_features.product_ready_features.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private String description;
}
```
Now, if we change the attribute from ‘id’ to ‘productId’ while the application is running.
```
package com.example.user.product_ready_features.product_ready_features.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;
    private String title;
    private String description;
}
```
And then send a POST request to create a product, you will see that the response still contains ‘id’ instead of ‘productId’.
<img width="1221" height="534" alt="image" src="https://github.com/user-attachments/assets/9f2bb307-6898-4cb8-97b5-b62ebf66cfc5" />

POST request to create a product still returns 'id' instead of 'productId' in the response
However, when you send a second request, the application automatically restarts, and the response will then include ‘productId’ instead of ‘id’. When working on it, ensure that spring.jpa.hibernate.ddl-auto=update is set in the application properties instead of create. If you set it to create, the productId will be null.
<img width="1215" height="528" alt="image" src="https://github.com/user-attachments/assets/3e45c1d8-5527-4b94-b5f2-2c303c04b52f" />

set update to prevent 'productId' from being null after an automatic restart
Automatic Restart
Automatic Restart is a feature of Spring Boot DevTools that enhances developer productivity by automatically restarting the application when it detects changes in the classpath. Instead of manually stopping and restarting the application after every code change, DevTools monitors the project files for updates (such as changes in classes or resources) and triggers a restart automatically.

Key aspects of Automatic Restart:
It applies only to the classpath entries that belong to the project, meaning changes in external libraries won’t trigger a restart.
Restarts are faster than a full application restart because Spring Boot only reloads the affected parts of the application, reusing the already loaded classes.
It helps maintain the application state in a development environment by allowing quick feedback from code changes.
Useful configurations for Dev-tools
spring.devtools.restart.enabled=false: Disables automatic restarts. Useful when you want to manually control restarts during development.
spring.devtools.restart.enabled=true enables the automatic restart feature in Spring Boot DevTools. When set to true, the application
will automatically restart when it detects changes in the classpath, such as modified classes or resources. This feature is particularly
useful during development, as it allows for quicker testing of changes without the need to manually restart the application.
spring.devtools.restart.exclude=static/**,public/**: Specifies patterns for files or directories that should be excluded from
triggering a restart. This helps avoid unnecessary restarts for static resources.
Navigate to a specific package in your project, right-click on it. 
<img width="1377" height="751" alt="image" src="https://github.com/user-attachments/assets/3fd6c1cd-3af8-41aa-a086-4a053fbad787" />

Ide image for configuring for Dev-tools
And select Copy Path/Reference.
<img width="771" height="277" alt="image" src="https://github.com/user-attachments/assets/2e927dff-5104-44a7-854f-68a64e05c76c" />

Image of copying the path of particular package
Then, copy the content root path and paste it into the value of spring.devtools.restart.exclude. Like this spring.devtools.restart.exclude=src/main/java/com/example/user/product_ready_features/product_ready_features/dtos/**
spring.devtools.restart.pollInterval=20: Sets the interval (in milliseconds) for checking changes in the classpath. A shorter interval means faster detection of changes but may increase CPU usage.
spring.devtools.restart.quietPeriod=10: Defines a delay (in milliseconds) before a restart is triggered after changes are detected. This helps avoid multiple rapid restarts when making several changes in quick succession.

Conclusion
This article explores Spring Boot DevTools, a powerful toolset designed to boost developer productivity with features like automatic 
restart, live reload, and property overrides. It simplifies the development process by enabling seamless testing and iteration 
without manual restarts. Proper configuration ensures efficient workflows and quick feedback for code changes.

