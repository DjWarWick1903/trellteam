package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Entity(name = "ORGANISATION")
@Table(name = "te_tr_organisation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "sign")
    private String sign;
    @Column(name = "CUI")
    private String CUI;
    @Column(name = "date_created")
    private Date dateCreated;
    @Column(name = "domain")
    private String domain;

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Department.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_org_dep_link",
            joinColumns = @JoinColumn(name = "id_org"),
            inverseJoinColumns = @JoinColumn(name = "id_dep")
    )
    private Set<Department> departments;

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Employee.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_org_emp_link",
            joinColumns = @JoinColumn(name = "id_org"),
            inverseJoinColumns = @JoinColumn(name = "id_emp")
    )
    private Set<Employee> employees;

    @Transactional
    public void addEmployee(final Employee employee) {
        if(employees == null) employees = new HashSet<>();
        employees.add(employee);
    }

    @Transactional
    public void removeEmployee(final Employee employee) {
        if(employees != null) employees.remove(employee);
    }

    @Transactional
    public void purgeEmployees() {
        if(employees != null) employees.clear();
    }

    @Transactional
    public void addDepartment(final Department department) {
        if(departments == null) departments = new HashSet<>();
        departments.add(department);
    }

    @Transactional
    public void removeDepartment(final Department department) {
        if(departments != null) {
            departments.remove(department);
        }
    }

    @Transactional
    public void purgeDepartments() {
        if(departments != null) {
            departments.clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return id.equals(that.id) && name.equals(that.name) && sign.equals(that.sign) && CUI.equals(that.CUI) && dateCreated.equals(that.dateCreated) && domain.equals(that.domain) && Objects.equals(departments, that.departments) && Objects.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sign, CUI, dateCreated, domain, departments, employees);
    }
}
