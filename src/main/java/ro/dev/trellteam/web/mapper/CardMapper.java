package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.web.dto.CardDto;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDto domainToDto(Card card);
    Card dtoToDomain(CardDto cardDto);
}
