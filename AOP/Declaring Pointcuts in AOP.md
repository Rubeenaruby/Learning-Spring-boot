Declaring Pointcuts in AOP

Introduction:

A pointcut in Spring Boot AOP (Aspect-Oriented Programming) is a predicate expression that identifies specific join points, or points 
during the execution of a program, where an aspect's advice (cross-cutting concerns like logging, security, etc.) should be applied. 
It allows developers to define conditions based on method signatures, annotations, parameters, or class types, enabling the separation
of cross-cutting concerns such as logging, validation, and transaction management from the core business logic. Pointcuts act as a 
filter to target specific methods or actions within an application, allowing for flexible and reusable application of advice
(such as @Before, @After, or @Around).

Imagine you’re a director working on a movie. The movie has multiple scenes, but you want to add some special effects or commentary at certain moments. Instead of applying these special effects to the entire movie, you define the pointcuts — these are the specific scenes (or moments) where the effects or commentary will be applied.

The pointcut acts like the selection of specific scenes based on certain criteria. For example, you might choose to add effects only to action scenes or commentary only to emotional moments.

The advice is the special effect or commentary that gets applied when the pointcut matches. So, when the action scene starts, your special effects kick in; when the emotional moment happens, the commentary begins.

Dependency:
When you use spring-boot-starter-data-jpa in a Spring Boot project, it includes Spring AOP as a transitive dependency. This means you don't need to explicitly add spring-boot-starter-aop to your dependencies if you're already using JPA.

   <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
However, the ability to "Ctrl + click" or navigate to see dependencies depends on your IDE or build tool configuration.

Aop dependency
However, if your project does not include JPA, you must explicitly add the Spring AOP dependency:

   <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
Using pointcuts in Aspect-Oriented Programming (AOP) allows you to modularize cross-cutting concerns by targeting specific methods, classes, or behaviors. This modularization improves code readability, maintainability, and reusability.

Custom Classes Code For Pointcuts Test:
Service Class Code From Custom Service Package:

@Slf4j
@Service
public class ShipmentServiceImpl {

    public String orderPackage(Long orderId) {

        log.info("Order package is called from ShipmentService");

        try {
            log.info("Processing the order...");
            Thread.sleep(1000);   //for testing purpose 
        } catch (InterruptedException e) {
            log.error("Error occurred while processing the order", e);
        }
        return "Order has been processed successfully, orderId: "+orderId;
    }
    
    @Transactional
    public String trackPackage(Long orderId) {

        log.info("Track package is called");

        try {
            log.info("Tracking the order...");
            Thread.sleep(500);
            throw new RuntimeException("Exception occurred during trackPackage");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
  }
Test Class Code For Testing Purpose:

@SpringBootTest
@Slf4j
class ShipmentServiceImplTest {

    @Autowired
    private ShipmentServiceImpl shipmentService;

    @Test
    void aopTestOrderPackage() {
        String orderString = shipmentService.orderPackage(4L);
        log.info(orderString);
    }
    
    @Test
    void aopTestTrackPackage() {
        shipmentService.trackPackage(4L);
    }
 }
Different kinds of pointcuts in Spring AOP with examples and explanations:
1. Execution Kind Pointcut
This is the most widely used pointcut expression. It targets method executions based on a specified pattern, such as the method signature, return type, method name, and parameter types.

Match All Methods in a Package

To match all methods in the com.example.service package:

@Pointcut("execution(* com.example.service.*.*(..))")
public void allServiceMethods() {}
Match Specific Method
To match the method findById in com.example.service.UserService:

@Pointcut("execution(public User com.example.service.UserService.findById(Long))")
public void specificMethod() {}
Match All Public Methods
To match all public methods in the application:

@Pointcut("execution(public * *(..))")
public void allPublicMethods() {}
Pointcut Syntax Explanation
General Syntax:
execution(modifiers-pattern? return-type-pattern declaring-type-pattern? method-name-pattern(param-pattern) throws-pattern?)
Simplified Syntax:
execution(return-type-pattern package.class.method-name(param-pattern))
CODE:
Logging Aspect Class Code From Custom Aspect Package:

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    //points cut

@Before("execution(* orderPackage(..))")  //'*' is return type (..) is any number of argument

     public void beforeOrderPackage(JoinPoint joinPoint) {
       log.info("Before calling orderPackage from LoggingAspect");
       //or
      // log.info("Before method call {}", joinPoint.getSignature());  //you can get the signature
      //or
      // log.info("Before called from LoggingAspect kind, {}", joinPoint.getKind()); //you can get the kind
       
    }
    
    }
JoinPoint: A point in the execution of a program where an aspect can be applied (e.g., method execution).

Specific method with specific package and class.
@Before("execution(* com.codingshuttle.week1.week1.services.impl.ShipmentServiceImpl.orderPackage(..))") 
Any method of specific package with specific class.
@Before("execution(* com.codingshuttle.week1.week1.services.impl.ShipmentServiceImpl.*(..))")
// or
@After("execution(* com.codingshuttle.week1.week1.services.impl.ShipmentServiceImpl.*(..))")
Try to find out orderPackage() method if it is present inside all classes of all impl packages.
@Before("execution(* com.codingshuttle.week1.week1.services.impl.*.orderPackage(..))") 
Any method of any class.
@Before("execution(* com.codingshuttle.week1.week1.services.impl.*.*(..))") 
//or
@After("execution(* com.codingshuttle.week1.week1.services.impl.*.*(..))")
Output:
Execution Kind Pointcut
Explanation:
execution: Specifies that the pointcut targets method executions.
return-type-pattern: Defines the return type of the method ( matches any type).
package.class.method-name: Specifies the fully qualified name of the method ( matches any class or method name).
param-pattern: Matches method parameters. Use (..), (*), or specific parameter types.
2. Within Kind Pointcut
The within() pointcut allows you to limit the advice to a particular class or package, without focusing on specific methods. It applies to all join points (methods, fields, constructors) within the target class or package.

Syntax:
within(<package or class>)
Class-level: Apply to all methods of a specific class.
Package-level: Apply to all methods within classes in a package.
Apply Advice to All Methods in a Class:
@Pointcut("within(com.example.service.UserService)")
public void allMethodsInUserService() {}
Apply Advice to All Methods in a Package:
@Pointcut("within(com.example.service..*)")
public void allMethodsInServicePackage() {}
Applying Advice to All Methods in Subclasses:
@Pointcut("within(com.example.service.*Service)")
public void allServiceMethods() {}
Applying Advice to All Classes and Subclasses:
@Pointcut("within(com.example..*)")
public void allServiceMethods() {}
Explanation:
within: Specifies the target class or package where the advice should apply. It is often used to apply advice to all methods in a particular class or package without needing to define specific method signatures.
Scope: It applies advice to all join points (methods, constructors, fields) within the specified class or package.
Code:
Logging Aspect Class Code From Custom Aspect Package:

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    //points cut

@Before("within(com.codingshuttle.week1.week1.services.impl.*)")

    public void beforeServiceImplCalls() {
        log.info("Service Impl calls");
    }
    
    }
Output:
Within Kind Pointcut
Explanation:
within: Specifies the target class or package.
This will apply advice to all methods within any class inside the specified package.
3. Annotation Kind Pointcut
Use @annotation() to apply advice to methods annotated with a particular annotation. This is useful when you want to apply advice only to methods that carry a specific annotation, whether built-in or custom.

Syntax:
@annotation(<annotation>)
Applying Advice to Methods with a Custom Annotation:
Custom Annotation:
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyLogging {
}
Aspect to Apply Advice:
@Aspect
@Component
@Slf4j
public class LoggingAspect {

@Before("@annotation(com.codingshuttle.week1.week1.aspects.MyLogging)")
public void beforeTransactionalAnnotationCalls() {
        log.info("Before MyLogging Annotation calls");
    }

}
Using the Annotation:
@Slf4j
@Service
public class ShipmentServiceImpl {

@MyLogging
    public String orderPackage(Long orderId) {

        log.info("Order package is called from ShipmentService");

        try {
            log.info("Processing the order...");
            Thread.sleep(1000);   //for testing purpose 
        } catch (InterruptedException e) {
            log.error("Error occurred while processing the order", e);
        }
        return "Order has been processed successfully, orderId: "+orderId;
    }
  }
Output:
Applying Advice to Methods with a Custom Annotation
Applying Advice to Methods with Built-in Annotations
Using the Transactional Annotation:
@Slf4j
@Service
public class ShipmentServiceImpl {
    
    @Transactional
    public String trackPackage(Long orderId) {

        log.info("Track package is called");

        try {
            log.info("Tracking the order...");
            Thread.sleep(500);
            throw new RuntimeException("Exception occurred during trackPackage");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
  }
Aspect to Apply Advice:
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")  // Apply advice to methods annotated with @Transactional
    public void beforeTransactionalAnnotationCalls() {
        log.info("Before Transactional Annotation calls");
    }
}
Output:
Applying Advice to Methods with Built-in Annotations
Explanation:
@annotation: Specifies that the advice should be applied to methods that are annotated with a given annotation.
The pointcut expression matches methods that carry a specific annotation, such as @MyLogging, @Transactional, or any custom annotations you define.
4. Pointcut Declaration
A pointcut can be declared separately using the @Pointcut annotation and then referenced in the advice annotations like @Before, @After, @Around, etc.

Syntax:
@Pointcut("<pointcut expression>")
public void <methodName>() {}
Reusable Pointcut for a Package
Declaring the Pointcut:
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceLayerMethods() {}
Sing the Pointcut in Advice:
@Before("serviceLayerMethods()")
public void logBeforeServiceMethod(JoinPoint joinPoint) {
    System.out.println("Executing method: " + joinPoint.getSignature().getName());
}

@After("serviceLayerMethods()")
public void logAfterServiceMethod(JoinPoint joinPoint) {
    System.out.println("Completed method: " + joinPoint.getSignature().getName());
}
Combining Pointcuts:
You can combine pointcuts using logical operators (&&, ||, !).

Declare Multiple Pointcuts:
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceLayerMethods() {}

@Pointcut("within(com.example.repository..*)")
public void repositoryLayerMethods() {}
Combine Pointcuts:
@Pointcut("serviceLayerMethods() || repositoryLayerMethods()")
public void applicationMethods() {}
Apply Combined Pointcut:
@Around("applicationMethods()")
public Object logApplicationMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("Method: " + joinPoint.getSignature().getName());
    return joinPoint.proceed();
}
By defining pointcuts separately and referencing them in the corresponding advice, Spring AOP allows you to modularly apply cross-cutting concerns such as logging, transaction management, and security.

Key Notes

The (..) pattern in the pointcut allows matching methods with any number or type of arguments.
The * wildcard can be used to match any return type, class name, or method name.
Combining multiple pointcuts using &&, ||, or! provides more flexibility.
Conclusion
This article provides an overview of pointcuts in Aspect-Oriented Programming (AOP) within Spring Boot, highlighting their role in separating cross-cutting concerns like logging, security, and transactions. It explains different types of pointcuts (execution, within, annotation, and reusable) and their syntax. Using pointcuts improves application modularity, scalability, and maintainability by decoupling concerns from core business logic.

Want to Master Spring Boot and Land Your Dream Job?
Struggling with coding interviews? Learn Data Structures & Algorithms (DSA) with our expert-led course. Build strong problem-solving skills, write optimized code, and crack top tech interviews with ease

Last updated on Jan 14, 2025

Was it helpful?
Subscribe to our newsletter
Read articles from Coding Shuttle directly inside your inbox. Subscribe to the newsletter, and don't miss out.

Enter your email address
We recommend



#BetterEveryday
follow us on
company
Resources
Courses
Popular Handbooks
Online Compilers
DSA Sheets
Mock Tests
Copyright © NGU Education Pvt. Ltd.

Declaring Pointcuts in AOP | Coding Shuttle
