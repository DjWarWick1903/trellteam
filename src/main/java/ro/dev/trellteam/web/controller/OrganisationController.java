package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.OrganisationDto;
import ro.dev.trellteam.web.dto.TypeDto;
import ro.dev.trellteam.web.request.organisation.RegisterOrganisationRequest;
import ro.dev.trellteam.web.response.ObjectResponse;
import ro.dev.trellteam.web.service.OrganisationService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/organisation/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrganisationController {
    private final OrganisationService organisationService;

    @GetMapping("/main/{username}")
    public ResponseEntity<ObjectResponse> getUserOrganisation(@PathVariable String username) {
        log.debug("OrganisationController--getUserOrganisation--IN");
        if(username == null || username.isEmpty()) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final OrganisationDto organisation = organisationService.getUserOrganisation(username);
        final ObjectResponse response = new ObjectResponse(organisation);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/main")
    public ResponseEntity<ObjectResponse> registerOrganisation(@RequestBody @Valid RegisterOrganisationRequest payload) {
        log.debug("OrganisationController--registerOrganisation--IN");
        final OrganisationDto organisationDto = organisationService.registerOrganisation(payload);
        final ObjectResponse response = new ObjectResponse(organisationDto);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/organisation/v1/main").toUriString());
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/type")
    public ResponseEntity<ObjectResponse> createType(@RequestBody @Valid TypeDto payload) {
        log.debug("OrganisationController--createType--IN");
        final TypeDto type = organisationService.createType(payload);
        final ObjectResponse response = new ObjectResponse(type);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/organisation/v1/type").toUriString());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/type/{idOrg}")
    public ResponseEntity<ObjectResponse> getOrganisationTypes(@PathVariable Long idOrg) {
        log.debug("OrganisationController--getOrganisationTypes--IN");
        if(idOrg == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final List<TypeDto> types = organisationService.getOrganisationTypes(idOrg);
        final ObjectResponse response = new ObjectResponse(types);
        return ResponseEntity.ok().body(response);
    }
}
