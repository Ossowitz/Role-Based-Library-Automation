package us.ossowitz.BootApplication.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.ossowitz.BootApplication.models.Book;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.services.BooksService;
import us.ossowitz.BootApplication.services.PeopleService;
import us.ossowitz.BootApplication.util.bookValidator.BookValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Api(description = "Контроллер для работы с Book")
public class BookController {

    private final BooksService booksService;

    private final PeopleService peopleService;

    private final BookValidator bookValidator;

    @GetMapping
    @ApiOperation("Получение списка всех book")
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
    @ApiOperation("Получение book по индексу")
    public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
        Book book = booksService.findOne(id);

        return (book != null)
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/info")
    @ApiOperation("Получение информации о book")
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
    @ApiOperation("Добавление book в базу данных")
    public ResponseEntity<?> addBook(@RequestBody @Valid Book book,
                                     BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        booksService.save(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Обновление book в базе данных")
    public ResponseEntity<?> updatePerson(@PathVariable("id") int id,
                                          @RequestBody @Valid Book updatedBook,
                                          BindingResult bindingResult) {
        bookValidator.validate(updatedBook, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Book book = booksService.findOne(id);

        if (book != null) {
            BeanUtils.copyProperties(updatedBook, book);
            booksService.update(id, book);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление book из базы данных")
    public ResponseEntity<?> deleteBook(@PathVariable("id") int id) {
        boolean isDeletedBook = booksService.delete(id);
        return isDeletedBook
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}