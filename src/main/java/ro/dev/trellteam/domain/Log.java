package ro.dev.trellteam.domain;

import lombok.*;

import javax.persistence.*;

@Entity(name = "LOG")
@Table(name = "te_tr_logs")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Long idAccount;
}
