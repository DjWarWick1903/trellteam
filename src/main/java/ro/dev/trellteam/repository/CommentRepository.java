package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.dev.trellteam.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
}
