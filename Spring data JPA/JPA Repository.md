JPA Repository Interface
Understanding the JpaRepository Interface in Spring Data JPA
The JpaRepository interface is one of the key components in Spring Data JPA, providing a complete abstraction over common database 
operations. By extending this interface, you can easily perform CRUD operations, pagination, sorting, and even custom queries, 
all without writing SQL code. In this article, we will explore the JpaRepository interface, its methods, and how to leverage it for
CRUD operations in a Spring Boot application.

1. What is JpaRepository?
The JpaRepository interface is part of the Spring Data JPA framework and extends both CrudRepository and PagingAndSortingRepository.
It adds JPA-specific methods for database operations and provides a wide range of pre-built methods to handle CRUD
(Create, Read, Update, Delete) functionality.

The general signature of the JpaRepository interface looks like this:
```
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    // Methods for CRUD operations
}
```
Here:

T is the entity type that will be managed.
ID is the type of the entity's identifier (usually Long or Integer).
By extending JpaRepository, you can directly call its methods to manage data in your database, without writing any SQL queries
or implementing complex logic.

2. Common Methods in JpaRepository
The JpaRepository interface provides several built-in methods to handle CRUD operations. Let’s explore some of the most commonly
 used methods.

save()
The save() method is used to either create a new entity or update an existing one:

Book savedBook = bookRepository.save(new Book("Spring Boot", "John Doe", 29.99));
If the entity’s id field is null, a new record will be inserted; otherwise, it will perform an update.

findById()
This method retrieves an entity by its primary key (id):
```
Optional<Book> book = bookRepository.findById(1L);
```
It returns an Optional, which is useful for avoiding null values. You can handle it like this:
```
book.ifPresent(b -> System.out.println(b.getTitle()));
```
findAll()
The findAll() method fetches all entities from the database:

List<Book> books = bookRepository.findAll();
This method returns a list of all Book entities.

delete()
To remove an entity from the database, you can use the delete() method:
```
bookRepository.deleteById(1L);
```
You can delete an entity by its ID or by passing the entire entity object.

3. Advanced Operations
Pagination and Sorting
JpaRepository also provides methods for pagination and sorting, which are helpful when working with large datasets.

Pagination: You can fetch data in chunks using findAll(Pageable pageable):
```
Page<Book> page = bookRepository.findAll(PageRequest.of(0, 10));
```
This fetches the first 10 records (page index starts from 0).

Sorting: You can sort the data by one or more fields using Sort:
```
List<Book> sortedBooks = bookRepository.findAll(Sort.by("title").ascending());
```
You can combine both pagination and sorting:
```
Page<Book> sortedPage = bookRepository.findAll(PageRequest.of(0, 5, Sort.by("price").descending()));
```
Custom Query Methods
You can define custom queries by following specific naming conventions in method names. For example:
```
List<Book> findByAuthor(String author);
List<Book> findByTitleContaining(String keyword);
```
Spring Data JPA automatically translates these method names into SQL queries. You can also use the @Query annotation to write custom JPQL (Java Persistence Query Language) queries:
```
@Query("SELECT b FROM Book b WHERE b.price > :price")
List<Book> findBooksCostlierThan(@Param("price") double price);
```
4. Example CRUD Application using JpaRepository
Let’s build a basic CRUD application using JpaRepository. Suppose we are working with a Book entity:
```
@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private double price;
}
```
Step 1: Define JpaRepository
First, create a repository interface that extends JpaRepository:
```
public interface BookRepository extends JpaRepository<Book, Long> {
}
```
Step 2: Service Layer
You can create a service class to manage business logic:
```
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book createOrUpdateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
```
Step 3: Controller Layer
Expose the CRUD operations via REST API using a controller:
```
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createOrUpdateBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        return bookService.createOrUpdateBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
```
With this setup, you have a fully functional CRUD application using JpaRepository that can handle database operations seamlessly.

In this article, we explored the JpaRepository interface, a vital component of Spring Data JPA that simplifies database operations. 
By extending JpaRepository, developers can perform CRUD operations, pagination, and custom queries without writing SQL code.
We discussed common methods such as save(), findById(), and delete(), and demonstrated how to create a simple CRUD application 
using a Book entity. With its powerful abstraction, JpaRepository allows for cleaner, more maintainable code, enabling you to focus
on your application’s business logic rather than database management.
