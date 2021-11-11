package app.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import app.error.generic.BaseError;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationError extends BaseError {
}
