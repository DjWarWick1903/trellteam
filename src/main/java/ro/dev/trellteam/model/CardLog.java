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
public class CardLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "log_date")
    private Date logDate;
    @Column(name = "type")
    private String type;
    @Column(name = "id_card")
    private Long idCard;
    @Column(name = "id_acc")
    private Long idAccount;
}
