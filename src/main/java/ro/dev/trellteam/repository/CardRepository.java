package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Card;
import ro.dev.trellteam.model.Type;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{

    Card save(Card card);
    List<Card> findAllByAssigned(Account assigned);
    List<Card> findAllByPublisher(Account publisher);
    List<Card> findAllByType(Type type);
    Optional<Card> findById(Long id);

}
