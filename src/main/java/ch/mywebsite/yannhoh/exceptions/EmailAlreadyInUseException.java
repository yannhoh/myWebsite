package ch.mywebsite.yannhoh.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "E-mail address is already in use")
public class EmailAlreadyInUseException extends Exception{ }

