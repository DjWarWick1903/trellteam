package ro.dev.trellteam.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "COMMENT")
@Table(name = "te_tr_card_comm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comm_text")
    private String text;
    @Column(name = "comm_date")
    private Date commentDate;

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
