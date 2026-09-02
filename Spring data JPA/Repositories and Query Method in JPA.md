Repositories and Query Methods in JPA

Repositories and Query Methods in JPA
Creating Custom Repositories, Extending JpaRepository, and Using Query Methods
When developing Spring Boot applications, repositories play a crucial role in handling database operations. With JPA
(Java Persistence API), Spring Data provides powerful abstractions for managing these operations, simplifying CRUD
(Create, Read, Update, Delete) operations and enabling custom queries with minimal effort.

1. What is a Repository in JPA?
A repository in JPA is a mechanism that encapsulates the logic required to access data sources. In Spring Data JPA, 
repositories provide a simple and efficient way to interact with the database, abstracting away much of the boilerplate code.

There are several types of repositories:

CrudRepository: Provides basic CRUD functionality.
JpaRepository: Extends CrudRepository and provides additional JPA-specific operations like flushing the persistence context 
and batch operations.
PagingAndSortingRepository: Extends CrudRepository and adds methods for pagination and sorting.
2. Extending JpaRepository
The JpaRepository interface provides out-of-the-box methods for all the common CRUD operations, eliminating the need to write 
queries manually.

Here’s how you can extend JpaRepository:

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

JpaRepository provides:
save(): Saves an entity.
findById(): Retrieves an entity by its ID.
findAll(): Retrieves all entities.
delete(): Deletes an entity.
Additional methods like flush() for synchronizing the persistence context and batch operations.
By extending JpaRepository, you inherit these methods without having to implement any logic yourself.

3. Creating Custom Repositories
Sometimes, the default methods provided by JpaRepository are not enough, and you may need to define custom behavior.

Step 1: Create a Custom Repository Interface
You can create a custom repository interface that contains the methods you want to define:

public interface CustomBookRepository {
    List<Book> findBooksByCustomCriteria(String criteria);
}
Step 2: Implement the Custom Repository
Then, you implement this interface in a custom class:
```
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class CustomBookRepositoryImpl implements CustomBookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findBooksByCustomCriteria(String criteria) {
        String query = "SELECT b FROM Book b WHERE b.title LIKE :criteria";
        TypedQuery<Book> typedQuery = entityManager.createQuery(query, Book.class);
        typedQuery.setParameter("criteria", "%" + criteria + "%");
        return typedQuery.getResultList();
    }
}
```
Step 3: Extend the Repository
Finally, extend both the JpaRepository and your custom repository in the repository interface:

public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {
}
4. Using Query Methods
Spring Data JPA allows you to define query methods based on the naming conventions of methods in your repository interface. You don't have to write complex JPQL queries manually; instead, Spring automatically generates the query from the method name.

Common Query Method Keywords
Some commonly used keywords for query methods:

findBy: Finds entities based on certain properties.
existsBy: Checks if an entity exists based on certain properties.
deleteBy: Deletes entities based on certain properties.
countBy: Counts entities based on certain properties.
Examples of Query Methods
Here’s how you can use query methods in a repository:

public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by title
    List<Book> findByTitle(String title);

    // Find books by title or author
    List<Book> findByTitleOrAuthor(String title, String author);

    // Find books by a certain price range
    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find books ordered by title
    List<Book> findByOrderByTitleAsc();
}
findByTitle(String title): Finds all books with the given title.
findByTitleOrAuthor(String title, String author): Finds books with a matching title or author.
findByPriceBetween(Double minPrice, Double maxPrice): Finds books within the given price range.
findByOrderByTitleAsc(): Finds books ordered by their titles in ascending order.
5. Examples of Query Methods
Example 1: Find Books by Title
// Repository method
List<Book> findByTitle(String title);

// Usage in service or controller
List<Book> books = bookRepository.findByTitle("Spring in Action");
This method retrieves all books with the title "Spring in Action".

Example 2: Find Books by Title or Author
// Repository method
List<Book> findByTitleOrAuthor(String title, String author);

// Usage in service or controller
List<Book> books = bookRepository.findByTitleOrAuthor("Spring", "Craig Walls");
This query retrieves books that either have the title "Spring" or are written by "Craig Walls".

Example 3: Custom Query using JPQL
// Custom query method in the repository
@Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
List<Book> searchBooksByKeyword(@Param("keyword") String keyword);

// Usage in service or controller
List<Book> books = bookRepository.searchBooksByKeyword("Hibernate");
This custom query searches for books with the keyword "Hibernate" in the title.

In this article, we explored how repositories in JPA simplify database operations in Spring Boot applications. By extending JpaRepository, you gain access to built-in CRUD methods, reducing boilerplate code. Custom repositories allow for tailored data access, while Spring Data JPA’s query methods enable easy construction of complex queries through method names.

Want to Master Spring Boot and Land Your Dream Job?
Struggling with coding interviews? Learn Data Structures & Algorithms (DSA) with our expert-led course. Build strong problem-solving skills, write optimized code, and crack top tech interviews with ease

Last updated on Dec 27, 2024

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

Repositories and Query Methods in JPA | Coding Shuttle
