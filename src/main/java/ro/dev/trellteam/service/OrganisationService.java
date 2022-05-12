package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.repository.OrganisationRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    /**
     * Method used to return the organisation starting from a provided name.
     * @param name
     * @return Organisation
     */
    public Organisation findByName(final String name) {
        log.debug("OrganisationService--Fetching organisation for name {}" , name);
        return organisationRepository.findByName(name);
    }

    /**
     * Method used to return the organisation starting from a provided id.
     * @param id
     * @return Organisation
     */
    public Organisation findById(final Long id) {
        log.debug("OrganisationService--Fetching organisation for id {}" , id);
        return organisationRepository.findById(id).get();
    }

    /**
     * Method used to save an organisation in the database.
     * @param organisation
     * @return Organisation
     */
    public Organisation save(final Organisation organisation) {
        log.debug("OrganisationService--Saving organisation");
        return  organisationRepository.save(organisation);
    }

    public Organisation saveAndFlush(final Organisation organisation) {
        log.debug("OrganisationService--Saving organisation");
        return  organisationRepository.saveAndFlush(organisation);
    }

    /**
     * Method used to delete an organisation from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("OrganisationService--Deleting organisation with id {}", id);
        organisationRepository.deleteById(id);
    }
}
