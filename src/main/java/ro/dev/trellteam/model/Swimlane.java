package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "SWIMLANE")
@Table(name = "te_tr_swimlane")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Swimlane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;
    @Column(name = "release")
    private Date release;

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Card.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "te_tr_card",
            joinColumns = @JoinColumn(name = "id_swl"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Card> cards;
}
