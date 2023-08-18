package us.ossowitz.BootApplication.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import us.ossowitz.BootApplication.util.personValidator.perkValidator.PerkPersonConstraint;

import java.util.List;

import static us.ossowitz.BootApplication.models.Perk.*;

@Entity
@Table(name = "person")
@Getter @Setter @NoArgsConstructor
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
    @PerkPersonConstraint(anyOf = {READERSHIP, LIBRARIAN})
    private Perk perk;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;
}