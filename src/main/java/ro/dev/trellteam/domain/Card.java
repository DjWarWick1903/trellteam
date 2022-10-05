package ro.dev.trellteam.domain;

import lombok.*;
import org.hibernate.annotations.SortComparator;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.comparator.CommentComparator;
import ro.dev.trellteam.comparator.LogComparator;
import ro.dev.trellteam.enums.CardStatusEnum;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity(name = "CARD")
@Table(name = "te_tr_card")
@Setter
@Getter
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
    private CardStatusEnum status;
    @Column(name = "urgency")
    private String urgency;

    @ManyToOne(
            targetEntity = Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_publisher")
    private Account publisher;

    @ManyToOne(
            targetEntity = Account.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_asigned")
    private Account assigned;

    @OneToOne(
            targetEntity = Type.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name="id_type")
    private Type type;

    @OneToMany(
            targetEntity = Comment.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_card_comm_link",
            joinColumns = @JoinColumn(name = "id_card"),
            inverseJoinColumns = @JoinColumn(name = "id_com")
    )
    @SortComparator(CommentComparator.class)
    private Set<Comment> comments;

    @OneToMany(
            targetEntity = CardLog.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "te_tr_card_log_link",
            joinColumns = @JoinColumn(name = "id_card"),
            inverseJoinColumns = @JoinColumn(name = "id_log")
    )
    @SortComparator(LogComparator.class)
    private Set<CardLog> logs;

    @Transactional
    public void addComment(final Comment comment) {
        if(comments == null) comments = new TreeSet<>();
        comments.add(comment);
    }

    @Transactional
    public void addLog(final CardLog log) {
        if(logs == null) logs = new TreeSet<>();
        logs.add(log);
    }
}