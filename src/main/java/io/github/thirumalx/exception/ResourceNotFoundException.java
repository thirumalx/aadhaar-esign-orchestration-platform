package io.github.thirumalx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Thirumal M
 *         Exception for resource not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = -6641460800722221525L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
