Setup a Spring boot application with MVC and JPA

Introduction

A Spring Boot application integrates several Spring projects, simplifying the development of Java applications.
Here's a brief introduction to its main components:

Spring Boot Web MVC:

Spring Boot Web MVC simplifies web development by providing an out-of-the-box setup for creating web applications. 
It follows the Model-View-Controller (MVC) pattern to separate concerns:

Model handles data (e.g., entities or domain objects).
View manages the UI (e.g., Thymeleaf, JSP).

Controller processes requests and returns views or data.
Spring Boot automatically configures essential components like embedded web servers (e.g., Tomcat) and provides easy handling of
RESTful endpoints.
Spring Boot Data JPA:
Spring Boot Data JPA simplifies data access and interaction with relational databases by integrating JPA (Java Persistence API).
It provides easy CRUD operations, pagination, and query methods.
JpaRepository offers built-in methods for interacting with the database, while custom query methods can be created with query
derivation or JPQL.
Build A Spring Boot Application
Go to , configure the necessary project settings as shown in the image, add the required dependencies, and generate the application
as a ZIP file.
<img width="1581" height="725" alt="image" src="https://github.com/user-attachments/assets/6059b17a-94f8-478f-9369-528d4e489db1" />


Springboot initializr

After that, go to File Explorer > Downloads, and extract the ZIP file.
After that, open the project in your preferred IDE. Here, we are using IntelliJ IDEA.
After opening the project, the first thing we need to do is reload Maven.
<img width="1581" height="836" alt="image" src="https://github.com/user-attachments/assets/bbcd4cff-a987-486b-a5ff-b974017b2ee8" />

Opening the project inside intellij

Now, let's add some packages. To do this, navigate to src > main > java > <group package> . After that, right-click on <group package>, 
select New > Package or Java Class, and create the necessary packages and classes.
<img width="1580" height="845" alt="image" src="https://github.com/user-attachments/assets/c2ee48ad-0108-4a11-bff1-7a0d2c553d6b" />

Creating new packages and classes inside intellij
In the same way, you can create different classes, interfaces, enums, and packages within your specific package.
showing the directory structure inside intellij
<img width="472" height="724" alt="image" src="https://github.com/user-attachments/assets/dfcfdefe-86a0-430d-aa77-060e6face6fb" />

Dto Class

Here, we are using @AllArgsConstructor and @NoArgsConstructor. These constructors are required by Jackson to convert JSON into DTO 
classes.
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
Entity Class

Here, we are using @AllArgsConstructor and @NoArgsConstructor because Hibernate requires these constructors to create entities and 
map database tables to entity objects.
```
package com.example.user.product_ready_features.product_ready_features.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
}
```
Repository Interface

This repository interface extends JpaRepository<ProductEntity, Long>, where ProductEntity is the entity type and Long is the type 
of the entity's ID.
```
package com.example.user.product_ready_features.product_ready_features.repositories;

import com.example.user.product_ready_features.product_ready_features.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
}
```
Controller Class
```
package com.example.user.product_ready_features.product_ready_features.controllers;

import com.example.user.product_ready_features.product_ready_features.dtos.ProductDto;
import com.example.user.product_ready_features.product_ready_features.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDto createNewProduct(@RequestBody ProductDto input){
        return productService.createNewProduct(input);
    }

}
```
Service Class

Here, we need to create the necessary constructor, or we can use @RequiredArgsConstructor from Lombok, which automatically 
generates the required constructors for us.
```
package com.example.user.product_ready_features.product_ready_features.services;

import com.example.user.product_ready_features.product_ready_features.dtos.ProductDto;
import com.example.user.product_ready_features.product_ready_features.entities.ProductEntity;
import com.example.user.product_ready_features.product_ready_features.exceptions.ResourceNotFoundException;
import com.example.user.product_ready_features.product_ready_features.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllProducts(){
        return productRepository.findAll()
                .stream().map(productEntity -> modelMapper.map(productEntity,ProductDto.class))
                .collect(Collectors.toList());
    }

    public ProductDto createNewProduct(ProductDto input){
        ProductEntity productEntity = modelMapper.map(input,ProductEntity.class);

        ProductEntity savedProductEntity = productRepository.save(productEntity);

        return modelMapper.map(savedProductEntity,ProductDto.class);
    }

    public ProductDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with id: "+id));

        return modelMapper.map(productEntity,ProductDto.class);
    }
}
```
Here, we use the DTO class for business logic, and we need to convert it to an Entity class. To do this, you should 
add the ModelMapper dependency and configure it as a bean.

Here, we need to handle all exceptions by creating custom exception classes.

Add dependency

Go to  and copy the dependency for the latest version.
<img width="1019" height="637" alt="image" src="https://github.com/user-attachments/assets/d1245e1c-7408-4b4f-bb1b-505edcba82c6" />

ModelMapper dependency
Add it in dependencies section of your Pom.xml file and after adding it, reload your maven.
```
<!-- https://mvnrepository.com/artifact/org.modelmapper/modelmapper -->
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.2.1</version>
</dependency
```
Configuration Class
```
package com.example.user.product_ready_features.product_ready_features.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    ModelMapper getMapper(){
        return new ModelMapper();
    }
}
```
Custom Exception Class
```
package com.example.user.product_ready_features.product_ready_features.exceptions;


public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
Dependencies and Plugins
```
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.modelmapper/modelmapper -->
		<dependency>
			<groupId>org.modelmapper</groupId>
			<artifactId>modelmapper</artifactId>
			<version>3.2.1</version>
		</dependency>

	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>
```
Database Setup
To store our data, we need to connect to a real database (e.g., MySQL, PostgreSQL). We can familiarize ourselves with industry-standard 
practices using applications like DBeaver. While there are several other applications available for connecting to multiple databases 
and running queries, DBeaver is popular among developers due to its versatility. Let’s explore how to use it after installing the 
software.

Installing MySql

Visit  page and for further guides you can check 

Installing DBeaver

Visit  download page and download the latest version for your preferred operating system. You can check this documentation for further guides  .
How to Connect to a Database in DBeaver:
Open DBeaver application
<img width="1193" height="840" alt="image" src="https://github.com/user-attachments/assets/34a8ebdb-d2f3-4d82-9e9b-697c9224dfe6" />

DBeaver
Go to New Database Connection and select the database you want to connect to
<img width="561" height="576" alt="image" src="https://github.com/user-attachments/assets/11bfcef6-9dcd-4d08-8658-9b4eae874ebc" />

Connecting new database inside intellij
Here, we have created a new database connection (MySQL) and clicked the Next button.
<img width="664" height="626" alt="image" src="https://github.com/user-attachments/assets/4f465fd7-cde1-4f44-a163-44f459772c7e" />

Selecting the particular database that we want
Here, you enter the password.
<img width="664" height="630" alt="image" src="https://github.com/user-attachments/assets/62d07718-9d03-46c7-b25c-a365ae59f141" />

Entering the password 
Click ‘Test Connection’. After testing, you will see a pop-up like the image. Press ‘OK.’
<img width="659" height="627" alt="image" src="https://github.com/user-attachments/assets/83691045-3c0d-4f2d-b880-595c2b7a3f42" />

Test connection
After that, check ‘Database Navigator’; you should see the localhost:3306 (MySQL) connection.
<img width="333" height="215" alt="image" src="https://github.com/user-attachments/assets/d2fec422-aea6-4b0b-b0fa-8ab1cf812177" />

Connected Databases
You need to check every time whether the connection is established. Essentially, ‘the green tick’ indicates that you are connected.
<img width="329" height="165" alt="image" src="https://github.com/user-attachments/assets/a29598fb-2508-4dfd-91e2-396b6896cbdc" />

Disconnected Database
If you see nothing, that means you are disconnected, and you will need to reconnect. To do this, right-click on it, and you will see the ‘Connect’ option.
<img width="536" height="478" alt="image" src="https://github.com/user-attachments/assets/57af8af2-7f39-4fdd-aaae-7f4690c2c4a5" />

How to connect Database
If you see a ‘red cross,’ that means you need to log in to MySQL again using the MySQL command line. After that, you can reconnect.
<img width="1029" height="607" alt="image" src="https://github.com/user-attachments/assets/4891fd51-4122-4ca2-b061-73a4bad3e67b" />

Public key retrieval is not allowed
How to Create a Database in DBeaver:
After connecting to MySQL, you can go to localhost:3306 > Databases. Right-click on Databases to create new databases.
<img width="604" height="302" alt="image" src="https://github.com/user-attachments/assets/0f9137b0-162d-49c0-af0f-015398de9ab6" />

Create a new Database
Go to ‘Create New Database,’ and enter the name of the database you want to create, then press ‘OK.’
<img width="314" height="183" alt="image" src="https://github.com/user-attachments/assets/6aa3d069-27bc-4356-ab59-60668ea3a8a9" />

Writing the Database name
Application Configure Database Connection
This configuration is crucial for connecting a Spring application to a MySQL database, defining how Hibernate interacts with that database, and providing useful logging for development. Adjustments should be made for production environments, especially concerning security and schema management. If you have specific areas you want to delve deeper into, let me know!

Go to resources > application.properties or resources > application.yml
<img width="466" height="523" alt="image" src="https://github.com/user-attachments/assets/50e228eb-6854-4a10-8971-197c797f3ea0" />


application.properties
Application Properties
<img width="877" height="314" alt="image" src="https://github.com/user-attachments/assets/05bcd99c-3b66-4a80-a0c4-01a76e58fe38" />

Configuring database

spring.application.name=product_ready_features

Purpose: Sets the name of the Spring application. This name can be useful for logging, monitoring, and managing different services in a microservices architecture.

Database Configuration

spring.datasource.url
spring.datasource.url=jdbc:mysql://localhost:3306/<Your Database Name>?useSSL=false

Purpose: Specifies the URL for the database connection. In this case:

jdbc:mysql:// indicates that the application will use the MySQL JDBC driver.
localhost:3306 refers to the database server running on the local machine at port 3306 (the default port for MySQL).
/<Your Database Name> is the name of the database to connect to.
?useSSL=false disables SSL for the connection, which can be useful in a development environment where SSL is not set up.
spring.datasource.username
spring.datasource.username=root

Purpose: Specifies the username to connect to the database. In this case, it is set to root, which is the default administrative user for MySQL.

spring.datasource.password
spring.datasource.password=<your mysql login password>

Purpose: Sets the password for the specified username to connect to the database. Ensure that sensitive information like passwords is handled securely (e.g., using environment variables or a secrets management tool).

JPA Configuration
spring.jpa.hibernate.ddl-auto
spring.jpa.hibernate.ddl-auto=create

Purpose

: Configures the behavior of the Hibernate framework regarding the database schema:

create: Drops the existing schema and creates a new one on application startup. This is useful during development but should be changed to update or none in production to avoid data loss.
spring.jpa.show-sql

spring.jpa.show-sql=true

Purpose: Enables the logging of SQL statements generated by Hibernate. This is useful for debugging and understanding what queries are being executed against the database.

spring.jpa.properties.hibernate.format_sql

spring.jpa.properties.hibernate.format_sql=true

Purpose: Formats the SQL output for better readability in the logs. When set to true, it indents the SQL statements, making it easier to read in the log files.

Run Application
Go to main application file and go to run button to run it.
<img width="1463" height="674" alt="image" src="https://github.com/user-attachments/assets/13984fd4-36b6-4528-848a-c49e4d8d21ed" />

Output
Output of IDE Console:
Output of IDE Console
Output of Postman Console:
For creating a new product, getting all products, getting product by product id, we are basically sending the request through Postman.
Create New Product
<img width="1206" height="589" alt="image" src="https://github.com/user-attachments/assets/075c09e9-96a4-4f61-b32c-d4796a65acbd" />

Create new product (Post Mapping)
Get All Products
<img width="1184" height="722" alt="image" src="https://github.com/user-attachments/assets/27759c20-1f21-4c16-9691-694ed6b2d6dc" />

Get all products (Get Mapping)
Get Product By Id
If the ID is present, the product is displayed.
<img width="1218" height="588" alt="image" src="https://github.com/user-attachments/assets/63e69ac2-d29a-4e7d-a953-b7b50d7e79a0" />

Get product by id (Get Mapping)
If the ID is not present, an internal server error is returned.
<img width="1219" height="525" alt="image" src="https://github.com/user-attachments/assets/e137f1ac-a8ce-48e3-8efd-ea0378cada6d" />

Getting internal server error (Get Mapping)
To handle this server-related exception, we need to create a custom exception handler class (GlobalExceptionHandler).
 
ApiError Class
```
package com.example.user.product_ready_features.product_ready_features.advices;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data

public class ApiError {
    private LocalDateTime timestamp;
    private String error;
    private HttpStatus status;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus status) {
        this();
        this.error = error;
        this.status = status;
    }
}
```
GlobalExceptionHandler
```
package com.example.user.product_ready_features.product_ready_features.advices;


import com.example.user.product_ready_features.product_ready_features.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }
   }
```
In this class, we will specifically handle the 404 Not Found error.
After running the application, if you send an invalid request, it will display a 404 Not Found error.
<img width="1189" height="677" alt="image" src="https://github.com/user-attachments/assets/d20119b5-1c58-48c5-a938-5be1936804de" />

Getting 404 not found error
Output of Database:

After running applications
<img width="845" height="744" alt="image" src="https://github.com/user-attachments/assets/bec0340b-024f-4097-aadc-1dcc79df09fa" />

After running the application the table is created
After creating new product. Let’s see our database.
<img width="822" height="724" alt="image" src="https://github.com/user-attachments/assets/198084cb-157d-4a6f-913e-9cc7d69e16bb" />

After creating new products 
Conclusion
This article explains how to build a Spring Boot application with MVC and JPA, covering entities, services, controllers, and exception handling. It also highlights using ModelMapper for DTO conversions and best practices for maintainability. Spring Boot's simplicity and scalability make it ideal for modern Java development.
