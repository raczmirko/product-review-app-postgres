package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @JsonIgnore
    @Column(nullable = false, length = 1000)
    private String password;

    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    @Column(name = "registered", nullable = false)
    private ZonedDateTime registrationDate;

    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @Column(name = "active", nullable = false)
    private Boolean isActive;

    public User() {
        this.isActive = true;
    }
}
