package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Comment;
import ro.dev.trellteam.web.repository.CommentRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    /**
     * Method used to save a comment in the database.
     * @param comment
     * @return Comment
     */
    public Comment save(Comment comment) {
        log.debug("CommentService--save--IN");

        comment = commentRepository.save(comment);

        log.debug("CommentService--save--comment: {}", comment);
        log.debug("CommentService--save--OUT");
        return comment;
    }

    /**
     * Method used to find a comment in the database starting from it's id.
     * @param id
     * @return Comment
     */
    public Comment findById(Long id) {
        log.debug("CommentService--findById--IN");
        log.debug("CommentService--findById--id: {}", id);

        final Comment comment = commentRepository.findById(id).get();

        log.debug("CommentService--findById--comment: {}", comment);
        log.debug("CommentService--findById--OUT");

        return comment;
    }
}
