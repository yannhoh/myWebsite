package ch.mywebsite.yannhoh.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Username is already taken")
public class UsernameAlreadyInUseException extends Exception{ }