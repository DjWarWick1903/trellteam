package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.*;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.OrganisationDto;
import ro.dev.trellteam.web.dto.TypeDto;
import ro.dev.trellteam.web.mapper.AccountMapper;
import ro.dev.trellteam.web.mapper.OrganisationMapper;
import ro.dev.trellteam.web.mapper.TypeMapper;
import ro.dev.trellteam.web.repository.OrganisationRepository;
import ro.dev.trellteam.web.request.organisation.RegisterOrganisationRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganisationService {
    private final TransactionalOperations transactionalOperations;
    private final OrganisationRepository organisationRepository;
    private final AccountService accountService;
    private final TypeService typeService;
    private final OrganisationMapper organisationMapper;
    private final AccountMapper accountMapper;
    private final TypeMapper typeMapper;

    public List<TypeDto> getOrganisationTypes(final Long idOrganisation) {
        log.debug("OrganisationService--getOrganisationTypes--idOrganisation: {}", idOrganisation);
        final List<Type> types = typeService.listTypesByOrganisation(idOrganisation);
        return types.stream()
                .map(t -> typeMapper.domainToDto(t))
                .collect(Collectors.toList());
    }

    public TypeDto createType(TypeDto request) {
        log.debug("OrganisationService--createType--request: {}", request);
        Type type = typeMapper.dtoToDomain(request);
        type = typeService.save(type);

        return typeMapper.domainToDto(type);
    }

    public OrganisationDto getUserOrganisation(final String username) {
        log.debug("OrganisationService--getUserOrganisation--username: {}", username);
        final Account account = accountService.getAccount(username);
        final Organisation organisation = findByEmployeeId(account.getEmployee().getId());

        return organisationMapper.domainToDto(organisation);
    }

    public OrganisationDto registerOrganisation(final RegisterOrganisationRequest request) {
        log.debug("OrganisationService--registerOrganisation--request: {}", request);
        Organisation organisation = organisationMapper.dtoToDomain(request.getOrganisationDto());
        Account account = accountMapper.dtoToDomain(request.getAccountDto());
        Department department = new Department(null, request.getDepartmentName(), null, null);

        return organisationMapper.domainToDto(transactionalOperations.createOrganisationRepository(organisation, department, account.getEmployee(), account));
    }

    /**
     * Method used to return the organisation starting from a provided name.
     * @param name
     * @return Organisation
     */
    public Organisation findByName(final String name) {
        log.debug("OrganisationService--findByName--IN");
        log.debug("OrganisationService--findByName--name: {}", name);

        Organisation organisation = null;
        try {
            organisation = organisationRepository.findByName(name);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_6");
        }

        log.debug("OrganisationService--findByName--organisation: {}", organisation);
        log.debug("OrganisationService--findByName--OUT");
        return organisation;
    }

    /**
     * Method used to retrieve an organisation based on a department's id.
     * @param id
     * @return Organisation
     */
    public Organisation findByDepartmentId(final Long id) {
        log.debug("OrganisationService--findByDepartmentId--IN");
        log.debug("OrganisationService--findByDepartmentId--id: {}", id);

        Organisation organisation = null;
        try {
            organisation = organisationRepository.findByDepartmentId(id);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_6");
        }

        log.debug("OrganisationService--findByDepartmentId--organisation: {}", organisation);
        log.debug("OrganisationService--findByDepartmentId--OUT");
        return organisation;
    }

    public Organisation findByEmployeeId(final Long id) {
        log.debug("OrganisationService--findByEmployeeId--IN");
        log.debug("OrganisationService--findByEmployeeId--id: {}", id);

        Organisation organisation = null;
        try {
            organisation = organisationRepository.findByEmployeeId(id);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_6");
        }

        log.debug("OrganisationService--findByEmployeeId--organisation: {}", organisation);
        log.debug("OrganisationService--findByEmployeeId--OUT");
        return organisation;
    }

    /**
     * Method used to return the organisation starting from a provided id.
     * @param id
     * @return Organisation
     */
    public Organisation findById(final Long id) {
        log.debug("OrganisationService--findById--IN");
        log.debug("OrganisationService--findById--id: {}", id);

        Organisation organisation = null;
        try {
            organisation = organisationRepository.findById(id).get();
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_6");
        }

        log.debug("OrganisationService--findById--organisation: {}", organisation);
        log.debug("OrganisationService--findById--OUT");

        return organisation;
    }

    /**
     * Method used to save an organisation in the database.
     * @param organisation
     * @return Organisation
     */
    public Organisation save(Organisation organisation) {
        log.debug("OrganisationService--save--IN");
        organisation = organisationRepository.save(organisation);
        log.debug("OrganisationService--save--OUT");

        return organisation;
    }

    /**
     * Method used to delete an organisation from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("OrganisationService--deleteById--IN");
        log.debug("OrganisationService--deleteById--id: {}", id);
        organisationRepository.deleteById(id);
        log.debug("OrganisationService--deleteById--OUT");
    }
}
