package ro.dev.trellteam.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ObjectResponse extends Response {
    private Object objectDto;
}
