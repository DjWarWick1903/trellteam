package ro.dev.trellteam.domain;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Entity(name = "ACCOUNT")
@Table(name = "te_tr_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "date_created")
    private Date dateCreated;
    @Column(name = "disabled")
    private Integer disabled;

    @OneToOne(
            targetEntity = Employee.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_acc_emp_link",
            joinColumns = @JoinColumn(name = "id_acc"),
            inverseJoinColumns = @JoinColumn(name = "id_emp")
    )
    private Employee employee;

    @ManyToMany(
            targetEntity = Role.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_acc_role_link",
            joinColumns = @JoinColumn(name = "id_acc"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<Role> roles;

    @Transactional
    public void addRole(final Role role) {
        if(roles == null) {
            roles = new ArrayList<>();
            roles.add(role);
        } else {
            Set<Role> roleSet = new HashSet<>(roles);
            roleSet.add(role);
            roles = new ArrayList<>(roleSet);
        }
    }

    @Transactional
    public void removeRole(final Role role) {
        if(roles != null) {
            roles.remove(role);
        }
    }
}
