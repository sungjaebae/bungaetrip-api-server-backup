package GoGetters.GoGetter.util;

import GoGetters.GoGetter.dto.response.Error;
import GoGetters.GoGetter.dto.response.Success;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity errorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new Error(message, status));
    }

    public static <T> ResponseEntity successResponse(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new Success<>(data));
    }
}
