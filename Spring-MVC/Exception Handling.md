Exception Handling

Exception Handling in Spring Boot: Custom Error Pages and @ExceptionHandler


Handling exceptions gracefully is crucial for building robust web applications. 
In this article, we'll explore how to implement custom error pages and use the @ExceptionHandler annotation to handle exceptions 
in a Spring Boot application. We'll cover the basics and provide practical examples to help you get started.

1. Understanding Exception Handling in Spring Boot
Exception handling in Spring Boot allows you to manage errors that occur during the execution of your application.
 By default, Spring Boot provides basic error handling, but you can customize it to fit your needs.

3. Implementing Custom Error Pages
Custom error pages can enhance the user experience by providing friendly and informative messages when something goes wrong.
To implement custom error pages in Spring Boot:

2.1 Create Error Pages
First, create HTML files for your error pages. Place them in the src/main/resources/templates directory. For example, 
you might create the following files:

400.html for "Bad Request" errors
500.html for "Internal Server Error" errors
Example of 400.html:
```
<!DOCTYPE html>
<html>
<head>
    <title>Bad Request</title>
</head>
<body>
<h1>400 - Bad Request</h1>
<p th:text="${errorMessage}">Sorry, this is a Bad Request.</p>
</body>
</html>
```
Example of 500.html:
```
<!DOCTYPE html>
<html>
<head>
    <title>Server Error</title>
</head>
<body>
<h1>500 - Internal Server Error</h1>
<p>Oops! Something went wrong on our end.</p>
<p th:text="${errorMessage}"> We are working on it</p>
</body>
</html>
```
Here ${errorMessage} will be passed from Exception handler as model attribute.

2.2 Configure Error Pages
Spring Boot automatically serves these pages when an error occurs. You can also configure specific error mappings in your application.properties file if needed.

Example Configuration in application.properties:
```
server.error.whitelabel.enabled=false
```
This disables the default Spring Boot error page and uses your custom error pages instead.

3. Using @ExceptionHandler to Handle Exceptions
The @ExceptionHandler annotation allows you to handle exceptions at the controller level. You can define methods in your controllers to handle specific exceptions and return custom responses.

3.1 Basic Usage of @ExceptionHandler
Create a controller and use @ExceptionHandler to manage exceptions.

Example:
```java
package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/divide/{a}divideBy{b}")
    public int divide(@PathVariable int a, @PathVariable int b) {
        return a / b; // This might throw ArithmeticException when b is 0
    }
    
    @GetMapping("/custom")
    public String custom() {
        throw new CustomException("This is a custom exception");
    }
}
```
```
class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}

```
Create CustomException Class this allow you to define application-specific error scenarios with more meaningful or 
context-specific messages.

In this example:

The MyController class has a method divide that may throw an ArithmeticException.
The GlobalExceptionHandler class uses @ExceptionHandler to handle ArithmeticException and return a custom error message.

3.2 Handling Multiple Exceptions
You can also handle multiple exceptions in a single @ExceptionHandler method.

Example:
```
@ControllerAdvice
class GlobalExceptionHandler {

		@ExceptionHandler({ArithmeticException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMultipleExceptions(Exception ex, Model model) {
        String message = "An error occurred: " + ex.getMessage();
        model.addAttribute("errorMessage",message);
        return "400.html";
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, Model model) {
        String message = "An error occurred: " + ex.getMessage();
        model.addAttribute("errorMessage",message);
        return "400.html";
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleInternalServerError(Exception ex, Model model) {
        String message = "An error occurred: " + ex.getMessage();
        model.addAttribute("errorMessage",message);
        return "500.html";
    }
}
```
In this example, both ArithmeticException and NullPointerException are handled by the same method.

@ControllerAdvice is a powerful feature in Spring Framework used for centralized exception handling, global data binding,
and model attributes. It acts as an interceptor that applies to all controllers in your application, allowing you to handle exceptions,
define global model attributes, or bind global data across multiple controllers without repeating the same code.

@ExceptionHandlerallows you to define custom logic to deal with exceptions and return a suitable response when an exception is thrown.

Run the application and access the enpoint from the browser.
http://localhost:8081/divide/1divideBy0
<img width="1913" height="1030" alt="image" src="https://github.com/user-attachments/assets/60dfc5f4-076f-4ef8-9ec8-ea08f5b854ca" />

Error Page
http://localhost:8081/custom
<img width="1917" height="1028" alt="image" src="https://github.com/user-attachments/assets/b8feb9cd-a4ea-46ce-98aa-ceafbc405a59" />

Custom Exception Page
In this article, we delved into exception handling in Spring Boot, focusing on implementing custom error pages and utilizing the 
@ExceptionHandler annotation for effective error management. By creating user-friendly error pages and customizing error handling
at the controller level, you can significantly enhance the user experience and maintain application robustness.
With practical examples, we demonstrated how to define and manage exceptions gracefully, ensuring your application responds 
appropriately to various error scenarios.
