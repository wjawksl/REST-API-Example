package com.example.restfulwebservice.user;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//HTTP Status code
// 2XX -> Ok
// 4XX -> Client error
// 5XX -> Server error

@ResponseStatus(HttpStatus.NOT_FOUND) // 4XX로 전달됨
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
