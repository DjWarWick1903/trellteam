package ro.dev.trellteam.comparator;

import ro.dev.trellteam.model.Comment;

import java.util.Comparator;

public class CommentComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment c1, Comment c2) {
        return c2.getCommentDate().compareTo(c1.getCommentDate());
    }
}