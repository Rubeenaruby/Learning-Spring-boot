JPQL and Native Queries

JPQL and Native Queries in JPA
Writing Custom Queries Using JPQL and Native SQL
In Spring Data JPA, sometimes the out-of-the-box repository methods may not be sufficient for certain complex queries. 
In such cases, you can write your own custom queries using JPQL (Java Persistence Query Language) or native SQL.

1. What is JPQL?
JPQL stands for Java Persistence Query Language. It is a platform-independent query language that works on entity objects rather
 than directly on database tables. JPQL is similar to SQL, but the main difference is that it works on entity classes and their
attributes, not directly on database tables or columns.

Key Characteristics of JPQL:
Queries entities and their relationships, not database tables.
It uses the same syntax as SQL but operates on the object model.
Supports features like joins, grouping, and ordering.
2. Writing JPQL Queries
In JPA, we can define JPQL queries using the @Query annotation in repository interfaces.

Example 1: Simple JPQL Query
This query selects all books where the title contains a certain keyword.
```
@Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
List<Book> searchBooksByTitleKeyword(@Param("keyword") String keyword);
```
Here:

b represents the Book entity.
The query searches for books with a title that contains the keyword (the % indicates wildcard matching).
Example 2: JPQL with Multiple Conditions
```
@Query("SELECT b FROM Book b WHERE b.author = :author AND b.price < :maxPrice")
List<Book> findBooksByAuthorAndPrice(@Param("author") String author, @Param("maxPrice") Double maxPrice);
```
This query finds all books written by a particular author and where the price is below a given maximum price.

3. What are Native Queries?
Native queries are SQL queries that are executed directly on the database. Unlike JPQL, which is database-agnostic, native queries use the actual SQL dialect of the underlying database. Native SQL is useful when you need to write a complex query that JPQL doesn’t support or when you need to optimize query performance.

4. Writing Native Queries
Native SQL queries can also be defined using the @Query annotation, but with the nativeQuery attribute set to true.

Example 1: Simple Native SQL Query
@Query(value = "SELECT * FROM book WHERE title LIKE %:keyword%", nativeQuery = true)
List<Book> searchBooksByTitleNative(@Param("keyword") String keyword);
Here:

The query operates directly on the book table in the database, not on the Book entity.
We’re using standard SQL syntax to search for books where the title contains a specific keyword.
Example 2: Native Query with Joins
```
@Query(value = "SELECT b.* FROM book b INNER JOIN author a ON b.author_id = a.id WHERE a.name = :authorName", nativeQuery = true)
List<Book> findBooksByAuthorNative(@Param("authorName") String authorName);
```
This native SQL query joins the book and author tables to find all books written by a particular author.

5. JPQL vs Native SQL: When to Use What?
Use JPQL:
When your query needs to be independent of the database.
For simple to moderately complex queries that involve entity relationships.
When you want the benefits of ORM and object-oriented querying.
Use Native SQL:
When the query is too complex for JPQL or involves database-specific functions.
When performance optimization is critical, and you want to leverage database-specific features.
If you need to interact with non-entity tables or execute advanced SQL that JPQL doesn’t support.
6. Examples of JPQL and Native Queries
Example 1: JPQL to Find All Books by Author
```
@Query("SELECT b FROM Book b WHERE b.author = :author")
List<Book> findBooksByAuthor(@Param("author") String author);
```
This JPQL query retrieves all books written by a specific author.

Example 2: JPQL Query with Ordering
```
@Query("SELECT b FROM Book b ORDER BY b.title ASC")
List<Book> findAllBooksOrderedByTitle();
```
This query selects all books and orders them by their title in ascending order.

Example 3: Native SQL Query for Finding Books by Price Range
```
@Query(value = "SELECT * FROM book WHERE price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
List<Book> findBooksByPriceRangeNative(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
```
This native query retrieves books within a specific price range from the book table.

Example 4: Native SQL Query for Complex Aggregation
```
@Query(value = "SELECT author, COUNT(*) FROM book GROUP BY author", nativeQuery = true)
List<Object[]> findAuthorBookCount();
```
This query counts the number of books per author using native SQL and returns the result as a list of objects.

In this article, we explored the usage of JPQL (Java Persistence Query Language) and native SQL queries within JPA
(Java Persistence API) to execute custom database queries in a Spring Data JPA application. We discussed the fundamental 
differences between JPQL and native queries, demonstrated how to write both types of queries using the @Query annotation, 
and provided practical examples for various querying scenarios. Understanding when to use JPQL versus native queries allows
developers to harness the power of JPA while also optimizing performance and complexity when necessary, enabling more robust and 
efficient data access in applications.
