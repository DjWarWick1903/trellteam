package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Log;
import ro.dev.trellteam.web.dto.LogDto;

@Mapper(componentModel = "spring")
public interface LogMapper {
    Log dtoToDomain(LogDto logDto);
    LogDto domainToDto(Log log);
}
