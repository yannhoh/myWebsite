package ch.mywebsite.yannhoh.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "A user with this id does not exist")
public class UserDoesNotExistException extends Exception {

}

