package ro.dev.trellteam.web.mappers;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.web.dto.BoardDto;

@Mapper
public interface BoardMapper {
    BoardDto domainToDto(Board board);
    Board dtoToDomain(BoardDto boardDto);
}
