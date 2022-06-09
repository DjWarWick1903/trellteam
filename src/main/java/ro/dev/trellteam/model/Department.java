package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "DEPARTMENT")
@Table(name = "te_tr_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @OneToOne(
            targetEntity = ro.dev.trellteam.model.Employee.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_man")
    private Employee manager;

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Employee.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_emp_dep_link",
            joinColumns = @JoinColumn(name = "id_dep"),
            inverseJoinColumns = @JoinColumn(name = "id_emp")
    )
    private Set<Employee> employees;

    @Transactional
    public void addEmployee(Employee employee) {
        if(employees == null) employees = new HashSet<>();
        employees.add(employee);
    }

    @Transactional
    public void removeEmployee(Employee employee) {
        if(employees != null) {
            employees.remove(employee);
        }
    }

    @Transactional
    public void purgeEmployees() {
        if(employees != null) {
            employees.clear();
        }
    }
}
