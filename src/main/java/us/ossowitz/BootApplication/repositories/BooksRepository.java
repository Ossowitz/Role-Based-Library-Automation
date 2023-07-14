package us.ossowitz.BootApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.ossowitz.BootApplication.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String title);

    boolean existsByTitle(String title);

    boolean existsByVendorCode(int vendorCode);
}
