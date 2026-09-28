Auditing and Advance Auditing with Hibernate Evers

Introduction

Auditing in Spring Boot is a feature that helps track and manage the changes made to your entities, providing insights into who made
changes and when. It can automatically populate fields like:

Creation Timestamp: The date and time when an entity was created.
Last Modified Timestamp: The date and time when an entity was last updated.
Created By: The user who created the entity.
Last Modified By: The user who last modified the entity.
To enable auditing, you typically use the @EnableJpaAuditing annotation in your configuration class. You also define an AuditorAware implementation to retrieve the current user, which can be based on the security context or other mechanisms.

A real-life analogy for auditing in an application is a security camera system in a building.

Here’s how it relates:

Tracking Actions: Just as a security camera records who enters or leaves a building and what activities occur, auditing records who created, updated, or deleted data in your system.

Time Stamping: Security cameras store the exact time and date of every event, similar to how auditing automatically logs timestamps for when a record was created or modified.

Accountability: If something goes wrong or is suspicious (like a break-in), you can review the footage to see who was responsible. Similarly, in an application, auditing allows you to see who made changes to data, helping ensure accountability.
Monitoring and Reviewing: In both cases, whether it's security footage or audit logs, you can go back and review the logs when needed for investigations or compliance.

Just like a building’s security system helps protect and keep track of activity within a building, auditing helps safeguard data and maintain a detailed record of changes within an application.

Benefit of Using Auditing in an Application

The primary benefit of using auditing in an application is enhanced data tracking and accountability. Here are some key advantages:

Change History: Auditing allows you to maintain a record of changes made to data, including who made the change and when it occurred. This is crucial for understanding the lifecycle of data and can help with troubleshooting.

Regulatory Compliance: Many industries have regulations that require tracking changes to sensitive data. Auditing can help meet these compliance requirements.

Accountability: By recording who performed actions on data, auditing promotes accountability within the organization. This can deter unauthorized changes and help identify issues quickly.

Data Integrity: Auditing can assist in maintaining the integrity of the data by providing a way to trace back changes, allowing for data recovery or correction if necessary.

User Activity Monitoring: It provides insights into user behavior and patterns, which can be valuable for security audits and improving user experience.

Reporting and Analysis: Auditing data can be used for reporting purposes, enabling analysis of how often certain data is accessed or modified.

Overall, auditing enhances the reliability and trustworthiness of your application by providing a transparent view of data changes and user interactions.

Steps to Add Auditing

Enable JPA Auditing:

Add the @EnableJpaAuditing annotation to a configuration class to activate the auditing feature.
```
package com.example.user.product_ready_features.product_ready_features.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class MapperConfig {

    @Bean
    ModelMapper getMapper(){
        return new ModelMapper();
    }
}
```
Create an Auditable Base Entity:

Create a base entity class that will include the auditing fields and annotations. Use @EntityListeners(AuditingEntityListener.class) to enable auditing.

This way you will get access to @CreatedBy, @CreatedDate, @LastModifiedBy, and @LastModifiedDate annotations.

Here we basically make the ProductEntity as a Audit table. And we add the

@EntityListeners(AuditingEntityListener.class) .
```
package com.example.user.product_ready_features.product_ready_features.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    @LastModifiedBy
    private String updatedBy;

}
```
Create Base Entity Class

In scenarios where we have many entity classes in our application, it is not efficient or maintainable to manually apply auditing annotations to each entity class. Instead, we can create a base auditable entity class that contains common audit fields like createdDate, lastModifiedDate, createdBy, and lastModifiedBy. Then, we can make all other entities inherit from this base class.
```
package com.example.user.product_ready_features.product_ready_features.entities;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity {

    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    @LastModifiedBy
    private String updatedBy;

}
```
Extend All Entities from the Superclass:

Ensure that all your entities extend this base Auditable class. After creating the Auditable base entity superclass (AuditableEntity), you need to extend all entities that require auditing from this superclass. This ensures that the common audit fields like createdDate, lastModifiedDate, createdBy, and lastModifiedBy are automatically included in the entities and managed by Spring Data JPA’s auditing mechanism.

Here we use one entity class that is ‘ProductEnity’ which extend the super class.
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

public class ProductEntity extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

}
```
Implement the AuditorAware Interface:

Create a class that implements the AuditorAware interface to retrieve the current authenticated user from Spring Security.

Here, we use a different package ‘auths’ to create the class (AuditorAwareImpl ) that implements the AuditorAware interface.
```
package com.example.user.product_ready_features.product_ready_features.auths;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        //get security context
        //get authentication
        //get the principle
        //get the username
        return Optional.of("Alice");
    }
}
```
Pass the AuditorAware Bean Reference:

The @EnableJpaAuditing annotation automatically detects the AuditorAware bean you created, so you don’t need to pass it explicitly. Just ensure that your AuditorAware implementation is annotated with @Component.

Here, we use configuration class to create AuditorAware bean.
```
package com.example.user.product_ready_features.product_ready_features.configs;

import com.example.user.product_ready_features.product_ready_features.auths.AuditorAwareImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAware")  // here in auditorAwareRef we have to pass the method name that is used to create AuditorAware interface bean
public class MapperConfig {

    @Bean
    ModelMapper getMapper(){
        return new ModelMapper();
    }

    @Bean
    AuditorAware<String> getAuditorAware(){return new AuditorAwareImpl();
    }
}
```
Code Implementation of Update Information

Auditing for updating information involves tracking changes made to an entity by automatically capturing relevant metadata. In this context, fields like @CreatedDate, @CreatedBy, @LastModifiedDate, and @LastModifiedBy serve specific purposes:

@CreatedDate: This field records the timestamp when the entity was first created, providing a reference for when the data was added to the system.

@CreatedBy: This field captures the identity of the user who created the entity, enhancing accountability by linking actions to specific users.

@LastModifiedDate: This field tracks the most recent timestamp when the entity was updated, allowing you to know when changes occurred last.

@LastModifiedBy: This field records the identity of the user who last modified the entity, helping to maintain an audit trail of changes over time.

Together, these auditing fields facilitate effective monitoring of data changes, ensuring transparency and accountability within the application. They provide valuable insights for compliance, troubleshooting, and understanding the history of data modifications.

If we want to update anything, such as the title, description, or both, we need to use the @PutMapping. Here’s the controller and service layer code.

Update Product Endpoint Controller Layer
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

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                    @RequestBody ProductDto input){
        return productService.updateProduct(id,input);

    }

}
```
updateProduct Method in the Service Layer
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

    public ProductDto updateProduct(Long id, ProductDto input) {
        ProductEntity olderProductEntity = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+id));

        input.setId(id);
        modelMapper.map(input,olderProductEntity);
        ProductEntity savedProductEntity = productRepository.save(olderProductEntity);

        return modelMapper.map(savedProductEntity,ProductDto.class);

    }
}
```
Output

PostMan:

<img width="1220" height="490" alt="image" src="https://github.com/user-attachments/assets/3dff246a-1688-4196-8ba3-3dd5a8317e4f" />

This section can include the expected response or result from a request made via Postman, such as the JSON response body, status code, and any relevant headers.

Image of creating a new product in postman

Databse:

Here, you can describe the expected changes in the database after the operation, including updated records, new entries, or any relevant queries to demonstrate the data state.

Before Update id 3 
<img width="1155" height="238" alt="image" src="https://github.com/user-attachments/assets/6d496c97-093c-45d4-928c-75e7509b2ade" />

Image of dbeaver before updating id 3

After Update id 3

<img width="1151" height="286" alt="image" src="https://github.com/user-attachments/assets/c1386f15-01f0-489c-a3f0-2f84cbe5c611" />

Image of dbeaver after updating id 3

Internal working of Auditing

The auditing feature in Spring Boot automates the tracking of entity changes by leveraging lifecycle event listeners and the AuditorAware interface to capture user and timestamp information seamlessly. This approach enhances data integrity and accountability in your application without requiring additional boilerplate code.

Entity Lifecycle Events:

When an entity is persisted or updated, the AuditingEntityListener is triggered.

This listener handles the following lifecycle events:

@PrePersist: This method is called before an entity is saved for the first time. During this phase, the listener populates the fields annotated with @CreatedDate and @CreatedBy.

@PreUpdate: This method is invoked before an entity is updated. It sets the fields annotated with @LastModifiedDate and @LastModifiedBy.

@PreRemove: This method is called before the entity manager executes the remove() operation on the entity. It is an appropriate place for implementing any necessary cleanup or validation logic.

AuditorAware Interface:

The AuditorAware interface is crucial for providing the current user’s information during the auditing process.
When the auditing listener is triggered, it calls the getCurrentAuditor() method of the AuditorAware implementation you created.
This method typically retrieves the current authenticated user's information, often using Spring Security's context to ensure that the correct user is logged in.
Hibernate Envers
Hibernate Envers is a module of Hibernate that provides auditing and versioning capabilities for entity classes. It allows you to track changes to your entities over time by creating a history of their states. Here’s a brief overview of its features and how it works:

Key Features of Hibernate Envers:
Audit History: Envers automatically creates audit tables for your entities, storing historical data each time an entity is created, modified, or deleted.
Versioning: It keeps track of all changes, allowing you to retrieve past versions of an entity, making it useful for data recovery and compliance.
Simple Integration: Envers integrates seamlessly with Hibernate and requires minimal configuration to get started.
Customizable: You can customize which fields to audit and how to handle specific actions (like ignoring certain fields or excluding certain entities).
Querying Historical Data: It provides APIs to easily query historical data, making it straightforward to access previous states of entities.
How Hibernate Envers Works:
Add Dependency
```
<!-- <https://mvnrepository.com/artifact/org.hibernate/hibernate-envers> -->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-envers</artifactId>
			<version>6.6.1.Final</version>
		</dependency>
```
Entity Configuration:

We annotate our entity classes with @Audited. This indicates that Hibernate should track changes to these entities. Here, we add @PrePersist , @PreUpdate , @PreRemove. The @Audited annotation in Hibernate Envers is used to mark an entity class that you want to track for auditing purposes. When applied, it tells Envers to create an audit table for that entity, where it will store historical versions of the entity whenever changes are made.
```
package com.example.user.product_ready_features.product_ready_features.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
@Audited
public class ProductEntity extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @PrePersist
    void beforeSave(){

    }

    @PreUpdate
    void beforeUpdate(){

    }

    @PreRemove
    void beforeRemove(){

    }

}
```
Field Configuration:

We can apply @NotAudited at the field level within an entity class or at the class level for an entire entity. The @NotAudited annotation in Hibernate Envers is used to indicate that specific fields or entities should not be tracked or included in the audit history. When applied, any changes to the annotated field or entity will not be recorded in the corresponding audit tables.
```
package com.example.user.product_ready_features.product_ready_features.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
@Audited
public class ProductEntity extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @NotAudited
    private String description;

    @PrePersist
    void beforeSave(){

    }

    @PreUpdate
    void beforeUpdate(){

    }

    @PreRemove
    void beforeRemove(){

    }

}
```
Output

After running the application, we can observe that the relevant tables, including the audit tables created by Hibernate Envers, appear in our database. This confirms that the auditing features have been successfully integrated.
<img width="333" height="195" alt="image" src="https://github.com/user-attachments/assets/4189f711-0072-4f13-bd41-7238b9e8b531" />


Hibernate Envers audit tables created

PostMan

Post Request
<img width="1217" height="472" alt="image" src="https://github.com/user-attachments/assets/b7553b0c-ca15-4888-9e6b-6572cdfc216d" />

Image of post request

Put Request
<img width="1217" height="456" alt="image" src="https://github.com/user-attachments/assets/f73033eb-3db5-4cd4-a744-dca7701b623a" />

Image of put mapping

Database
<img width="1315" height="279" alt="image" src="https://github.com/user-attachments/assets/b70ced39-9aa8-41cd-8f86-58c7bc5958c6" />


Products with createdDat, createdBy and updatedDate, updatedBy.

Table of products_aud

       Table of revinfo.

Table of revinfo

<img width="1315" height="333" alt="image" src="https://github.com/user-attachments/assets/5541c223-a67d-46ac-b826-19a7f5cf8f26" />

When to Use Hibernate Envers:
Data Auditing:

When your application requires a history of changes made to entity data, such as in financial applications, content management systems, or any data-driven application where accountability is important.

Regulatory Compliance:

If you need to comply with legal or regulatory requirements that mandate tracking changes to data (e.g., healthcare, finance), Envers provides a straightforward way to maintain an audit trail.

Version Control:

When you need to keep track of different versions of an entity. This is useful for applications that need to provide the ability to roll back to previous states or review historical data.

Data Recovery:

In scenarios where data might be accidentally deleted or modified, having a historical record allows for easy restoration of previous states.

Change Tracking:

When you want to monitor who changed what and when, providing accountability and traceability for user actions in the application.

Why Use Hibernate Envers:

Automatic Change Tracking:

Envers automates the process of auditing by creating and managing audit tables, reducing the manual overhead associated with tracking changes.

Minimal Configuration:

Integrating Envers into your Hibernate application requires minimal setup, making it easy to add auditing features without extensive code changes.

Query Historical Data:

Envers provides a convenient API to query past versions of entities, allowing developers to easily access historical data without complex queries.

Customizable Auditing:

You can customize which fields to audit, ignore specific changes, and define custom revision entities to capture additional metadata.
Seamless Integration:

Envers integrates seamlessly with Hibernate, so you can continue using your existing entity model without significant alterations.
Limitation

Complexity and Overhead: It introduces additional complexity and a learning curve, and may impact performance in high-transaction environments.

Limited Flexibility: Some projects require highly customized auditing solutions that Envers may not provide.

Not Always Necessary: For simpler applications, the added complexity of Envers may be unnecessary, especially if existing solutions are in place.

Database Schema Changes: Adding audit tables can complicate database migrations and management.

Development and Maintenance: Maintaining audit tables can create operational overhead and manage data volume.

Compatibility Issues: Potential integration challenges with other frameworks may lead developers to seek alternative solutions.

Create Some APIs for Admin

Controller
```
package com.example.user.product_ready_features.product_ready_features.controllers;

import com.example.user.product_ready_features.product_ready_features.entities.ProductEntity;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @GetMapping("/products/{id}")
    public List<ProductEntity> getProductRevision(@PathVariable Long id){
        AuditReader auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());

        List<Number> revisions = auditReader.getRevisions(ProductEntity.class,id);

        return revisions.stream().map(revisionNumber -> auditReader.find(ProductEntity.class,id,revisionNumber))
                .collect(Collectors.toList());

    }
}
```
Output

After running the application.
<img width="1206" height="600" alt="image" src="https://github.com/user-attachments/assets/b41ed70a-7376-4b58-bac8-66f3ffbd04c3" />

PostMan: If you create a new product and update its information by hitting the GET/audit/products/{id} endpoint, you can view all the related data, including the updated product details.
Image of getting list of creating and updating product by id
Database:
<img width="1316" height="318" alt="image" src="https://github.com/user-attachments/assets/9962ab30-ca10-4e0b-85d8-ef1c83b456ba" />

Image of dbeaver

Conclusion

This article explains how to implement auditing and advanced auditing using Hibernate Envers in Spring Boot to track changes in 
data and maintain accountability. It covers enabling JPA auditing, using AuditorAware for user tracking, and integrating Hibernate Envers for entity versioning. The focus is on improving security, compliance, and traceability within applications.
