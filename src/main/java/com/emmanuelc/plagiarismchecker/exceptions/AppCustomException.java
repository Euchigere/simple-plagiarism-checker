package com.emmanuelc.plagiarismchecker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class AppCustomException extends RuntimeException {
    /**
     * For serialization: if any changes is made to this class, update the
     * serialVersionID
     */
    private static final long serialVersionUID = 1L;

    String message;

    HttpStatus status;

}
