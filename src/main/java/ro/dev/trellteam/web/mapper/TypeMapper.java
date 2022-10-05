package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Type;
import ro.dev.trellteam.web.dto.TypeDto;

@Mapper
public interface TypeMapper {
    Type dtoToDomain(TypeDto typeDto);
    TypeDto domainToDto(Type type);
}
