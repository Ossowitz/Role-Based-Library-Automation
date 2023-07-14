package us.ossowitz.BootApplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import us.ossowitz.BootApplication.util.personValidator.perkValidator.PerkPersonConstraint;

import java.util.List;

import static us.ossowitz.BootApplication.models.Perk.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @NotNull
    @Min(value = 0, message = "Age should be greater than 0")
    @Column(name = "age")
    private int age;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "^[А-Я][а-яА-Я]*,\\s[А-Я][а-яА-Я]*,\\s[А-Я][а-яА-Я]*$",
            message = "Your address should be in this format: Country, City, District")
    @Column(name = "address")
    private String address;

    @NotEmpty(message = "Phone number should not be empty")
    @Pattern(regexp = "^\\d{11}$",
            message = "This number already taken")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @PerkPersonConstraint(anyOf = {READERSHIP, MENTOR, VIP, STAFF})
    private Perk perk;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Perk getPerk() {
        return perk;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
