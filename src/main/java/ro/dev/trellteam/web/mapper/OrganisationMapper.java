package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Organisation;
import ro.dev.trellteam.web.dto.OrganisationDto;

@Mapper
public interface OrganisationMapper {
    Organisation dtoToDomain(OrganisationDto organisationDto);
    OrganisationDto domainToDto(Organisation organisation);
}
