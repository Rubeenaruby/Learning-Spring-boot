Restclient

Introduction

RestTemplate, WebClient, and RestClient are powerful HTTP clients in Java used for more than just third-party API calls. 
They enable seamless communication between services, making them ideal for both external APIs and internal service-to-service 
interactions in microservices or monolithic architectures. Their versatility allows for efficient data exchange, backend integration,
and even testing, providing scalable and maintainable solutions for modern applications.

RestTemplate: Still in use for legacy systems, but it’s deprecated and should be avoided for new development.

WebClient: The preferred choice for modern Spring applications, especially those utilizing reactive programming and requiring 
asynchronous processing.

RestClient: A newer, synchronous client that offers a modern API, expected to replace RestTemplate for developers who need 
blocking calls but want a more updated and flexible tool.

If you’re starting a new project today, WebClient is generally the best choice unless you specifically need a synchronous client, 
in which case RestClient is a solid modern alternative to RestTemplate. Here, we use RestClient.

A REST client is a software application or library that communicates with a RESTful web service to perform operations such as 
retrieving, creating, updating, or deleting resources over HTTP. REST (Representational State Transfer) is an architectural style 
that relies on stateless communication and standard HTTP methods (GET, POST, PUT, DELETE).

REST clients can be built in various programming languages and often provide features like:

HTTP Method Support: Ability to send requests using GET, POST, PUT, DELETE, etc.

Response Handling: Processing responses, including parsing JSON or XML data.

Error Handling: Managing error responses and exceptions.

Authentication: Supporting various authentication methods, such as OAuth, API keys, or Basic Auth.

Configuration: Allowing customization of headers, timeouts, and query parameters.

Popular libraries for building REST clients include Axios (JavaScript), RestTemplate (Spring), and Retrofit (Java).

The RestClient indeed simplifies the process of making HTTP requests in a more intuitive way. It streamlines the conversion between
Java objects and HTTP requests/responses, making it easier to work with APIs. With its fluent API, developers can chain methods
for cleaner and more readable code. This abstraction helps reduce boilerplate code, allowing developers to focus on the core 
functionality of their applications.

Third-Party API Calls
Third-party API calls involve interacting with external services outside your own application. These could be platforms like payment 
gateways, weather services, or social media APIs. By using tools like RestTemplate, WebClient, or RestClient, you can send HTTP 
requests (GET, POST, etc.) to these APIs, retrieve data, and integrate external functionality into your application, enhancing its
capabilities without building everything from scratch.

Need Two Applications
Here, we create two application

prod_ready_features application (which we have created in Production Ready Features Module)
 
employees application (we create a basic application using basic implementation)
Go to 
Create an application
<img width="1590" height="729" alt="image" src="https://github.com/user-attachments/assets/dd5846ca-1547-4170-b1ad-ce8f77099f80" />

Spring initializr
After generating the ZIP file, extract it and open it in your IDE.
After creating this application, perform some simple CRUD operations with the employees table for testing purposes. I am using only three operations: POST (for creating employees), GET (for retrieving the list of all employees), and GET by ID (for retrieving a specific employee by ID).
Also, set two different server ports to run them separately.
Applications Running Process
To run two applications simultaneously, each on a separate port, you need to configure them accordingly:

Separate Ports:
Ensure that each application is set to run on a distinct port (e.g., server.port=9090 for one application and server.port=8080 for the other).
API Calls:
When making calls from the prod_ready_features application to the Employees APIs, use the specific URL that includes the port where the Employees application is running (e.g., http://localhost:8080/employees).
Example Configuration:
Employees Application (Application Properties):
<img width="878" height="488" alt="image" src="https://github.com/user-attachments/assets/99b6d8d9-68bf-4783-baaf-ce2fe2b98801" />

Employees Application (Application Properties)
prod_ready_features Application (Application Properties):
<img width="1001" height="353" alt="image" src="https://github.com/user-attachments/assets/7189b42b-0ff5-46e3-80bb-1531d56e345b" />

prod_ready_features Application (Application Properties)

Building RestClient

Building a RestClient refers to the process of configuring and initializing a RestClient instance to interact with external APIs. In Spring, you can configure the RestClient to include properties like the base URL, default headers, and error handling.

RestClient restClient = RestClient.builder() .baseUrl(BASE_URL) .defaultHeader(HttpHeaders.AUTHORIZATION, encodeBasic(properties.getUsername(),properties.getPassword()) ) .build();

We create a RestClientConfig class.
```
package com.example.user.product_ready_features.product_ready_features.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    @Value("${employeeService.base.url}")
    private String BASE_URL;

    @Bean
    @Qualifier("employeeRestClient")
    RestClient getRestClient(){
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(CONTENT_TYPE,APPLICATION_JSON_VALUE)
                .build();
    }
}
```
The RestClientConfig class is a Spring configuration that sets up a RestClient bean for interacting with an employee service.
It uses the @Value annotation to inject the base URL from properties(Application properties), and the @Bean annotation to create a 
managed RestClient. The use of @Qualifier allows for specific bean injection when multiple beans are present. Make sure the base
URL property is defined in your configuration files.

Using the RestClient

Using the RestClient involves making HTTP requests (like GET, POST, PUT, DELETE) to interact with external APIs or internal services 
in your application. After configuring the RestClient in your Spring application, you can inject it into your services and use it to
send requests and handle responses.

CustomerResponse customer = restClient.get() .uri("/{id}",3) .accept(MediaType.APPLICATION_JSON) .retrieve()
.body(CustomerResponse.class); Apart from get(), we have post(), put(), patch() and delete() methods as well.

Here, we create a separate package ‘client’ to create a class where we use the RestClient.

Inject the RestClient: Use dependency injection to bring the RestClient instance into your service.
Make HTTP Requests: Use methods like get(), post(), put(), delete() to make requests.
Handle Responses: Parse and handle the response using methods like body() to deserialize it into Java objects.

Creating GET Request (Get Employee By Id)
```
@Service
@RequiredArgsConstructor
public class EmployeeClient {

    private final RestClient restClient;

   public EmployeeDto getEmployeeById(Long id) {
        try{
        ApiResponse<EmployeeDto> employeeDtoList = restClient
                .get()
                .uri("employees/{id}", id)
                .retrieve()
             
                .body(new ParameterizedTypeReference<>() {
                });
        return employeeDtoList.getData();
    }catch(
    Exception e)

    {
        throw new RuntimeException(e);
    }
}
}
```
Creating GET Request (Get All Employees)
```
@Service
@RequiredArgsConstructor
public class EmployeeClient {

    private final RestClient restClient;

    public List<EmployeeDto> getAllEmployees() {
        try {
            ApiResponse<List<EmployeeDto>> employeeDtoList = restClient.get()
                    .uri("/employees")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            return employeeDtoList.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
```
Creating POST Request (Create New Employee)
```
@Service
@RequiredArgsConstructor
public class EmployeeClient {

    private final RestClient restClient;

    public EmployeeDto createNewEmployee(EmployeeDto input) {
        try {
            ApiResponse<EmployeeDto> employeeDto = restClient
                    .post()
                    .uri("/employees")
                    .body(input)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });

            return employeeDto.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
```
Explanation

Creating a POST/GET request with restClient.post()/restClient.get().

Setting the URI with a placeholder for the customer ID, which is replaced by 3.

Specifying the expected response format using accept(MediaType.APPLICATION_JSON).

Retrieving the response with retrieve().

Mapping the response body directly to a CustomerResponse object. Here we use ParameterizedTypeReference.

ParameterizedTypeReference:

This is necessary when the response contains a generic type (e.g., ApiResponse<EmployeeDto>), so Java can correctly map the nested
types in the response.
The methods post(), put(), patch(), and delete() follow a similar pattern, allowing you to interact with the API easily. 
For example:

POST: Used to create a new resource.

PUT: Generally used to update an existing resource.

PATCH: Used for partial updates.

DELETE: Used to remove a resource.

Test API Calls
Here, we use the resclient for testing purpose. Go to the test directory and create test methods by using RestClient.

Test Method for POST Request

GET for employee by id
```
@SpringBootTest
class ProductReadyFeaturesApplicationTests {

	@Autowired
	private EmployeeClient employeeClient;

	@Test
	void getEmployeeById(){
		EmployeeDto employeeDto = employeeClient.getEmployeeById(1L);
		System.out.println(employeeDto);
	}
	}
```
GET for List of all employees
```
@SpringBootTest
class ProductReadyFeaturesApplicationTests {

	@Autowired
	private EmployeeClient employeeClient;

	@Test
	void getAllEmployees() {
		List<EmployeeDto> employeeDtoList = employeeClient.getAllEmployees();
		System.out.println(employeeDtoList);
	}
	}
```
Test Method for GET Requests
```
@SpringBootTest
class ProductReadyFeaturesApplicationTests {

	@Autowired
	private EmployeeClient employeeClient;

	@Test
	void createNewEmployee(){
		EmployeeDto employeeDto = new EmployeeDto(null,"Alice","alice@gmail.com",25, LocalDate.of(2024, 9, 10));
		EmployeeDto savedEmployeeDto = employeeClient.createNewEmployee(employeeDto);
		System.out.println(savedEmployeeDto);
	}
	}
```
OutPut
After running both applications.

Output of POST Request (Create New Employee)
Create New Employee
Output of GET Request (GET ALL Employee)
GET ALL Employee
Output of GET Request (GET Employee By Id)
GET Employee By Id
When I ran the application, if the endpoint is incorrect or the particular ID is not found, it gives us an error or exception.
Handling Errors in RestClient
Handling errors in RestClient is crucial for building resilient applications. You can handle various HTTP errors like client-side errors (4xx) and server-side errors (5xx) by configuring default error handlers in RestClient.

Here’s how to handle errors in RestClient effectively:

RestClient Error Handlers: Handle HTTP errors (4xx, 5xx) using .defaultStatusHandler().
Service Layer: Catch and manage errors using custom exceptions.
Custom Exception: Create meaningful exceptions for better error tracking.
Controller Error Handling: Send user-friendly error messages using a custom ErrorResponse DTO.
ResponseEntity response = restClient.get() .... .onStatus(HttpStatusCode::is4xxClientError, (req, res) ->

System.out.println(new String(res.getBody().readAllBytes()));

// or you can print it using logger

logger.error(new String(res.getBody().readAllBytes())); ) .toBodilessEntity();

Explanation
In this snippet, you're using a RestClient to make a GET request and handle 4xx client-side errors. The onStatus() method is used
to define how the response should be handled if the HTTP status code falls within the 4xx range (client errors).

Key Components:
onStatus():

This is a handler that gets invoked when a response matches a specific condition, in this case, a 4xx client error
(HttpStatusCode::is4xxClientError).
Error Handling:

When a 4xx error occurs, the code reads the response body using res.getBody().readAllBytes() and converts it to a string.
This string contains the error message or details sent by the server in the response body.
System.out.println() / logger.error():

You can either print the error response directly to the console (System.out.println()) or use a logger (logger.error()) to log 
the error details.
Logging is usually preferred for better error tracking in production environments.
toBodilessEntity():

This method is used when you are not expecting a response body (for example, for status-checking or delete operations), 
and you're only interested in the status of the request.
Implementation with Error Handling
RestClient Config

Error Handling: A default handler is defined for 5xx server errors, throwing a RuntimeException with a relevant message when such
errors occur.
```
@Service
@RequiredArgsConstructor
public class EmployeeClient {

    private final RestClient restClient;

       public EmployeeDto getEmployeeById(Long id) {
        try{
        ApiResponse<EmployeeDto> employeeDtoList = restClient
                .get()
                .uri("employees/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,(req, res)->{
		                System.out.println(new String(res.getBody().readAllBytes()));
                    throw new ResourceNotFoundException("Employee not found");
                })
                .body(new ParameterizedTypeReference<>() {
                });
        return employeeDtoList.getData();
    }catch(
    Exception e)

    {
        throw new RuntimeException(e);
    }
    }
```
EmployeeClient

Error Handling with onStatus():
The onStatus(HttpStatusCode::is4xxClientError) method checks for 4xx client errors (e.g., 400 Bad Request, 404 Not Found).
If a client error occurs, it logs the response body (error details) and throws a ResourceNotFoundException with a custom message
("could not create the employee").
```
@Configuration
public class RestClientConfig {

    @Value("${employeeService.base.url}")
    private String BASE_URL;

    @Bean
    @Qualifier("employeeRestClient")
    RestClient getRestClient(){
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(CONTENT_TYPE,APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,(req, res)->{
                    throw new RuntimeException("SERVER ERROR OCCURRED");
                })
                .build();
    }
}
```
Conclusion
This article provides an overview of using RestClient in modern Java applications for making synchronous HTTP requests. 
It compares RestClient with RestTemplate and WebClient, highlighting its advantages for blocking calls.
Practical examples of setting up and using RestClient in a Spring Boot environment are provided.
