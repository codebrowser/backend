package rage.codebrowser.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "I seem to have misplaced my thingies or you don't know what you're looking for :(...")
public class ResourceNotFoundException extends RuntimeException {
}
