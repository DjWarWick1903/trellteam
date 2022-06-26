package ro.dev.trellteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortComparator;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.comparator.CommentComparator;
import ro.dev.trellteam.comparator.LogComparator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
    @Column(name = "urgency")
    private String urgency;

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

    @OneToMany(
            targetEntity = ro.dev.trellteam.model.Comment.class,
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
            targetEntity = ro.dev.trellteam.model.CardLog.class,
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