package ro.dev.trellteam.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "EMPLOYEE")
@Table(name = "te_tr_employee")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "CNP")
    private String CNP;
    @Column(name = "bday")
    private Date bday;
}
