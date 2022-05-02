package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "NOTIFICATION")
@Table(name = "te_tr_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @Column(name = "date_created")
    private Date dateCreated;
    @Column(name="id_acc")
    private Long idAccount;
    @Column(name="is_seen")
    private Integer isSeen;
}
