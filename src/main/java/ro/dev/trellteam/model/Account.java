package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "ACCOUNT")
@Table(name = "te_tr_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private Date dateCreated;

    @ManyToMany(
            targetEntity = ro.dev.trellteam.model.Role.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_acc_role_link",
            joinColumns = @JoinColumn(name = "id_acc"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<Role> roles;

    public void addRole(Role role) {
        if(roles.isEmpty()) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }
}
