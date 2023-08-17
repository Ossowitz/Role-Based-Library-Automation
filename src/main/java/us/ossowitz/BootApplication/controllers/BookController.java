package us.ossowitz.BootApplication.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.ossowitz.BootApplication.models.Book;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.services.BooksService;
import us.ossowitz.BootApplication.services.PeopleService;
import us.ossowitz.BootApplication.util.bookValidator.BookValidator;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {

    private final BooksService booksService;

    private final PeopleService peopleService;

    private final BookValidator bookValidator;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "booksPerPage", required = false) Integer booksPerPage,
            @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        List<Book> books;
        if (page == null || booksPerPage == null) {
            books = booksService.findAll(sortByYear);
        } else {
            books = booksService.findWithPagination(page, booksPerPage, sortByYear);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
        Book book = booksService.findOne(id);

        return (book != null)
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<?> getPersonByBookId(@PathVariable("id") int id) {
        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner == null) {
            List<Person> people = peopleService.findAll();
            return new ResponseEntity<>(people, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookOwner, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody @Valid Book book,
                                     BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        booksService.save(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBook(@PathVariable("id") int id) {
        boolean isDeletedBook = booksService.delete(id);
        return isDeletedBook
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}