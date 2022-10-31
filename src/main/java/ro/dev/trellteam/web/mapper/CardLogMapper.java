package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.CardLog;
import ro.dev.trellteam.web.dto.CardLogDto;

@Mapper(componentModel = "spring")
public interface CardLogMapper {
    CardLog dtoToDomain(CardLogDto cardLogDto);
    CardLogDto domainToDto(CardLog cardLog);
}
