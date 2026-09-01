Fetching Strategies

Fetching Strategies in JPA: Optimizing Query Performance

When working with object-relational mapping (ORM) tools like JPA/Hibernate, handling the retrieval of associated entities efficiently
is critical. Fetching strategies help optimize database access and reduce unnecessary performance bottlenecks.

1. What are Fetching Strategies?
Fetching strategies in JPA determine how and when related entities (associations) are loaded from the database when querying an entity.
These strategies are essential for managing performance, particularly when dealing with large datasets and complex relationships.

JPA provides two main fetching strategies:

Eager Fetching: Loads related entities immediately along with the parent entity.
Lazy Fetching: Loads related entities on-demand, when they are accessed for the first time.
2. Eager vs. Lazy Fetching
Eager Fetching (FetchType.EAGER)
In eager fetching, related entities are fetched at the same time as the main entity. This is useful when you always need the 
associated data.

Pros: Reduces the number of queries because the related entities are fetched in a single query.
Cons: Can lead to unnecessary overhead if the related entities are not always required, resulting in performance issues.
Example:
```
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Eager fetching: Always fetches the books when fetching the author
    @OneToMany(fetch = FetchType.EAGER)
    private List<Book> books;
}
```
In this case, when an Author is fetched, all associated Book entities are also fetched, even if we don’t immediately need them.

Lazy Fetching (FetchType.LAZY)
In lazy fetching, the related entities are loaded on-demand. This means that they are fetched only when you try to access them for 
the first time.

Pros: Improves performance by loading related entities only when needed.
Cons: Can lead to the "N+1 select problem," where multiple queries are executed when accessing the lazy-loaded entities.
Example:
```
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Lazy fetching: Fetches the books only when accessed
    @OneToMany(fetch = FetchType.LAZY)
    private List<Book> books;
}
```
Here, the Book entities are not fetched until you explicitly access the books field of an Author object.

3. Fetching in Relationships
The fetching strategy can be defined in different relationships like OneToOne, OneToMany, ManyToOne, and ManyToMany.
Let's look at how to apply fetching strategies in these cases.

OneToOne Relationship
```
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private Profile profile;
}
```
In this OneToOne relationship between User and Profile, the Profile is eagerly loaded when the User entity is fetched.

OneToMany Relationship
```
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
```
In a OneToMany relationship, the OrderItems are fetched lazily. Only when orderItems is accessed, will they be fetched from the database.

4. Choosing the Right Fetching Strategy
Use Eager Fetching when you always need the associated entities along with the parent entity. For example, in a OneToOne relationship
 where you frequently access both sides together.
Use Lazy Fetching when associated entities are not always required and should only be fetched when accessed. This helps in optimizing
performance, especially in large collections like OneToMany.
However, beware of the N+1 Select Problem in lazy fetching. This occurs when lazy-loaded entities trigger multiple queries,
significantly increasing the number of database calls.

Example of N+1 Select Problem:
```
List<Author> authors = entityManager.createQuery("SELECT a FROM Author a").getResultList();
for (Author author : authors) {
    System.out.println(author.getBooks());  // This triggers a separate query for each author
}
```
In this example, fetching the list of Author objects triggers a separate query for each author's books, leading to multiple queries 
(N+1 problem).

5. Examples of Fetching Strategies
Example 1: Lazy Fetching in OneToMany
```
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Employee> employees;

    // Getter and Setter methods
}
```
Here, Department has a OneToMany relationship with Employee. Employees will only be fetched from the database when you 
access the employees field in a department object.

Example 2: Eager Fetching in ManyToOne
```
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Department department;

    // Getter and Setter methods
}
```
In this case, when you load an Employee entity, its associated Department is fetched eagerly.

In this article, we delved into fetching strategies in JPA, focusing on how they play a crucial role in optimizing query performance. 
By understanding the two main fetching strategies—Eager Fetching and Lazy Fetching—you can make informed decisions that enhance your 
application's efficiency and responsiveness.
