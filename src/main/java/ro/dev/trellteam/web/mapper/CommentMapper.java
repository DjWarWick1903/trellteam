package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Comment;
import ro.dev.trellteam.web.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment dtoToDomain(CommentDto commentDto);
    CommentDto domainToDto(Comment comment);
}
