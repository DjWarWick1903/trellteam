package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Type;
import ro.dev.trellteam.repository.TypeRepository;

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

        final List<Type> types = typeRepository.findByIdOrganisation(idOrganisation);

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

        final Type type = typeRepository.findById(id).get();

        log.debug("TypeService--findById--type: {}", type.toString());
        log.debug("TypeService--findById--OUT");

        return type;
    }
}
