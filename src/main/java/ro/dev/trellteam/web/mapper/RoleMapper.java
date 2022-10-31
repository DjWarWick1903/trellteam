package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.web.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role dtoToDomain(RoleDto roleDto);
    RoleDto domainToDto(Role role);
}
