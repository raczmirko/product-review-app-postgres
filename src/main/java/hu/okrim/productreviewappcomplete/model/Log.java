package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false, length = 128)
    private String userName;

    @Column(nullable = false, length = 6)
    private String dmlType;

    @Column(nullable = false, length = 100)
    private String tableName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
