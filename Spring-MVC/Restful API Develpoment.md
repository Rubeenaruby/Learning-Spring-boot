Restful API Development


Restful API Develpoment in Spring Boot

REST (Representational State Transfer) is a widely used architectural style for building APIs.
It leverages HTTP methods to perform operations on resources. In Spring Boot, building RESTful services is facilitated 
by annotations like @RestController, @RequestMapping, and the usage of HTTP methods like GET, POST, PUT, and DELETE.
In this article, we will explore how to build RESTful APIs using these tools.

Introduction to REST

REST is a stateless architecture, where each request from the client to the server must contain all the information needed
to understand and process the request. RESTful APIs follow the following key principles:

Statelessness: 

Each request is independent, and the server doesn't retain client state.
Uniform Interface: APIs use standard HTTP methods like GET, POST, PUT, and DELETE.
Client-Server Separation: The client and server are independent and can evolve separately.

1. Visit  and generate the employee service project and open in Intellij
We wil be building simple employee service with basic crud operations.

Spring boot initializer

2. Crete the following folders and files under your package 
Directory Structure of Spring Boot Employee applcation
3. Configuring Postgres Database
spring.application.name=TestingApp

#Port configuration
server.port=8081 //port on which your application will be running

#DB configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?useSSL=false //here postgres is our DB name
spring.datasource.username=postgres // username that you have given while installing postgres
spring.datasource.password=admin // password that you have specified while installing postgres
spring.jpa.hibernate.ddl-auto=create-drop //This will create schema on running your application and will also drop all schemas once you stop your application
spring.jpa.show-sql=true // TO see all the sql statements runned by hibernate 
spring.jpa.properties.hibernate.format_sql=true //Format the hibernate queries, statements
4. Adding model mapper configuration and dependency in pom.xml
<dependency>
	<groupId>org.modelmapper</groupId>
	<artifactId>modelmapper</artifactId>
	<version>3.2.0</version>
</dependency>

```java
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```
Add the following model mapper bean in AppConfig file, we will use model mapper to convert dto to entity and vise versa.

5. Create Employee Entity class

```java
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private Long salary;
}
```
7. Data Transfer Objects (DTOs)
DTOs are used to transfer data between the client and server. They contain only the necessary fields required for
data exchange.
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String email;
    private String name;
    private Long salary;
}
```
7. Creating Employee repository interface

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);
}
```
@Repository: This annotation is a specialization of the @Component annotation in Spring. It marks the class as a
Data Access Object (DAO), which means it’s responsible for handling the data persistence logic in the application. 
In this case, it’s the repository for the Employee entity.

The EmployeeRepository extends JpaRepository, which is a Spring Data JPA interface that provides standard CRUD operations
like saving, updating, deleting, and finding entities. By extending JpaRepository, we avoid manually implementing 
these basic database operations.
JpaRepository<Employee, Long> defines that this repository is for the Employee entity class, and Long is the type of 
the primary key (id).
List<Employee> findByEmail(String email); This is a custom query method provided by Spring Data JPA.
Without writing SQL, you can define custom queries by following a naming convention.

9. RESTful Services with Spring Boot, building employee controller
Spring Boot simplifies the development of RESTful services with annotations like @RestController and @RequestMapping.
Let's dive into an example:

```java
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployeeDto = employeeService.createNewEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployeeDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

```
In this example:

@RestController marks the class as a REST controller, meaning it will handle HTTP requests and return JSON responses.
@RequestMapping("/employees") defines the base path for all the endpoints in this controller.
@RequiredArgsConstructor is a Lombok annotation to automatically generate a constructor for final fields.

9. Controller Implementation
GET Method: Retrieving Data
The @GetMapping annotation is used for retrieving data. It fetches an employee by their ID:

```java
@GetMapping("/{id}")
public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
    EmployeeDto employeeDto = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employeeDto);
}
```

@PathVariable maps the {id} in the URL to the method parameter.
ResponseEntity.ok() returns an HTTP 200 response with the employee's data.
POST Method: Creating New Data
The @PostMapping is used to create a new employee:
```java
@PostMapping
public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto) {
    EmployeeDto createdEmployeeDto = employeeService.createNewEmployee(employeeDto);
    return new ResponseEntity<>(createdEmployeeDto, HttpStatus.CREATED);
}
```
@RequestBody binds the incoming JSON request body to the EmployeeDto.
The response is created with the CREATED status (HTTP 201).
PUT Method: Updating Existing Data
@PutMapping is used to update an existing employee:
```java
@PutMapping("/{id}")
public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
    EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
    return ResponseEntity.ok(updatedEmployee);
}
```
This method updates an employee's details and returns the updated employee object with an HTTP 200 status.

DELETE Method: Removing Data
@DeleteMapping is used for deleting an employee:
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
}
```
This method deletes an employee and returns an HTTP 204 (No Content) response if successful.

10. Service Layer
The service layer contains business logic and interacts with the repository layer.
```java
@Service
@RequiredArgsConstructor
public class EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        Employee newEmployee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        modelMapper.map(employeeDto, employee);
        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

    
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
```
ModelMapper is used to map EmployeeDto to Employee and vice versa.
This layer abstracts the business logic from the controller, making the code cleaner and more modular.
11. Crete new Resource not found exception class
```java
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
12. Exception Handling with @RestControllerAdvice
Handling exceptions in RESTful APIs is important for returning meaningful error messages. The @RestControllerAdvice annotation handles exceptions globally.
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.internalServerError().build();
    }
}
```
This advice handles ResourceNotFoundException and RuntimeException, ensuring that the client receives proper error responses.

12. Now Open post man to test our apis
1. Create Employee
Run your application, after running you can see in console the hibernate DDL queries to create the employee table.

Employee table DDL query
Now go to post man and hit the create employee endpoint

Create Employee
2. Get Employee by id
Get Employee by id
3. Update Employee by id
Update Employee by id
4. Delete Employee by id
Delete Employee by id
In this article, we covered the essential steps for building a RESTful API in Spring Boot, including setting up a basic
project, configuring a database, creating entity classes, and implementing CRUD operations through controllers and services. 
We also explored exception handling to make APIs more robust and user-friendly. By leveraging Spring Boot’s powerful 
features and annotations, you can rapidly develop scalable and maintainable RESTful services.

