package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Department.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "te_tr_department",
            joinColumns = @JoinColumn(name = "id_org"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Department> departments;
}
