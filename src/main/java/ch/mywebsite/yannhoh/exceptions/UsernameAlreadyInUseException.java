package ch.mywebsite.yannhoh.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Username is already in use")
public class UsernameAlreadyInUseException extends Exception{ }