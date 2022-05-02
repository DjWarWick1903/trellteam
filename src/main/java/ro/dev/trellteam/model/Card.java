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
    @Column(name = "type")
    private String type;
    @Column(name = "difficulty")
    private Integer difficulty;
    @Column(name = "description")
    private String description;
    @Column(name = "notes")
    private String notes;
    @Column(name = "idSwimlane")
    private Long idSwimlane;
    @JoinColumn(name = "id_publisher")
    private Account idPublisher;
    @JoinColumn(name = "id_asigned")
    private Account idAssigned;
}
