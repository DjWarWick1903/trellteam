package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.dev.trellteam.domain.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
}
