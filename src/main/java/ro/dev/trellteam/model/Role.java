package ro.dev.trellteam.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "ROLE")
@Table(name = "te_ma_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
