package app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import app.error.generic.BaseError;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictError extends BaseError {
}
