package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "CARD")
@Table(name = "te_tr_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "difficulty")
    private String difficulty;
    @Column(name = "description")
    private String description;
    @Column(name = "notes")
    private String notes;
    @Column(name = "status")
    private String status;

    @ManyToOne(
            targetEntity = ro.dev.trellteam.model.Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_publisher")
    private Account publisher;

    @ManyToOne(
            targetEntity = ro.dev.trellteam.model.Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_asigned")
    private Account assigned;

    @OneToOne(
            targetEntity = ro.dev.trellteam.model.Type.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name="id_type")
    private Type type;
}
