package us.ossowitz.BootApplication.util.bookValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import us.ossowitz.BootApplication.models.Book;
import us.ossowitz.BootApplication.services.BooksService;

@Component
public class BookValidator implements Validator {

    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        validateTitle(book, errors);
        validateVendorCode(book, errors);
    }

    private void validateVendorCode(Book book, Errors errors) {
        if (booksService.isTitleTaken(book.getTitle())) {
            errors.rejectValue("title", "", "This title already will be taken");
        }
    }

    private void validateTitle(Book book, Errors errors) {
        if (booksService.isVendorCodeExists(book.getVendorCode())) {
            errors.rejectValue("vendorCode", "", "This vendor already code will be taken");
        }
    }
}
