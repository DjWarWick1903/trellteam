package ro.dev.trellteam.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "CARDLOG")
@Table(name = "te_tr_card_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardLog implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "log_text")
    private String text;
    @Column(name = "log_date")
    private Date logDate;

    @OneToOne(
            targetEntity = Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name="id_acc")
    private Account user;

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
