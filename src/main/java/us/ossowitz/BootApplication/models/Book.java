package us.ossowitz.BootApplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "book")
@Getter @Setter @NoArgsConstructor
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 30, message = "Title should be between 2 and 30 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author name should not be empty")
    @Pattern(regexp = "^[a-zA-Z ]+$")
    @Column(name = "author")
    private String author;

    @NotNull
    @Min(value = 0, message = "Publication year should be greater than 0")
    @Column(name = "year")
    private int year;

    @Digits(integer = 6, message = "«VendorCode» should contain exactly 6 digits", fraction = 0)
    @Column(name = "vendor_code")
    private int vendorCode;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean expired;
}