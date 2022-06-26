package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "CARDLOG")
@Table(name = "te_tr_card_log")
@Data
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
            targetEntity = ro.dev.trellteam.model.Account.class,
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
