package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Type;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.repository.TypeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TypeService {
    private final TypeRepository typeRepository;

    /**
     * Method used to save a type inside the database.
     * @param type
     * @return Type
     */
    public Type save(Type type) {
        log.debug("TypeService--save--IN");

        type = typeRepository.save(type);

        log.debug("TypeService--save--type: {}", type.toString());
        log.debug("TypeService--save--OUT");

        return type;
    }

    /**
     * Method used to get a list of types of an organisation.
     * @param idOrganisation
     * @return List
     */
    public List<Type> listTypesByOrganisation(final Long idOrganisation) {
        log.debug("TypeService--listTypesByOrganisation--IN");

        List<Type> types = null;
        try {
            types = typeRepository.findByIdOrganisation(idOrganisation);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_1");
        }

        log.debug("TypeService--listTypesByOrganisation--type count: {}", types.size());
        log.debug("TypeService--listTypesByOrganisation--OUT");

        return types;
    }

    /**
     * Method used to get a type from the database using it's id.
     * @param id
     * @return Type
     */
    public Type findById(final Long id) {
        log.debug("TypeService--findById--IN");

        Type type = null;
        try {
            type = typeRepository.findById(id).get();
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_1");
        }

        log.debug("TypeService--findById--type: {}", type.toString());
        log.debug("TypeService--findById--OUT");

        return type;
    }
}
