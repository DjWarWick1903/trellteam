package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_emp_dep_link",
            joinColumns = @JoinColumn(name = "id_dep"),
            inverseJoinColumns = @JoinColumn(name = "id_emp")
    )
    private List<Employee> employees;

    public void addEmployee(Employee employee) {
        if(employees == null) employees = new ArrayList<Employee>();
        employees.add(employee);
    }
}
