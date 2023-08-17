package us.ossowitz.BootApplication.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.ossowitz.BootApplication.models.Book;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.services.BooksService;
import us.ossowitz.BootApplication.services.PeopleService;
import us.ossowitz.BootApplication.util.bookValidator.BookValidator;

import java.util.List;

@RestController
@RequestMapping("/books")
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

    @GetMapping("/{id}/info")
    public ResponseEntity<?> getPersonByBookId(
            @PathVariable("id") int id
    ) {
        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner == null) {
            List<Person> people = peopleService.findAll();
            return new ResponseEntity<>(people, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookOwner, HttpStatus.OK);
        }
    }
}
