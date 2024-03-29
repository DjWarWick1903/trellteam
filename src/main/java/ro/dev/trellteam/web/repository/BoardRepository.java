package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Board;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByIdDep(Long idDep);
    void deleteByTitle(String title);
    void deleteById(Long idBoard);
    Optional<Board> findById(Long id);
}
