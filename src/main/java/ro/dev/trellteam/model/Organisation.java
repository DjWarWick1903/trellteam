package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_org_dep_link",
            joinColumns = @JoinColumn(name = "id_org"),
            inverseJoinColumns = @JoinColumn(name = "id_dep")
    )
    private List<Department> departments;

    public void addDepartment(Department department) {
        if(departments == null) departments = new ArrayList<>();
        departments.add(department);
    }
}
