package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.web.dto.BoardDto;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardDto domainToDto(Board board);
    Board dtoToDomain(BoardDto boardDto);
}
