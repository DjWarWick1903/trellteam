package ro.dev.trellteam.web.mappers;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Log;
import ro.dev.trellteam.web.dto.LogDto;

@Mapper
public interface LogMapper {
    Log dtoToDomain(LogDto logDto);
    LogDto domainToDto(Log log);
}
