package com.haribo.notification_service.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse<T> {

    private HttpStatus statusCode;
    private String resultMsg;
    private T resultData;

    // Static factory method for success response
    public static <T> NotificationResponse<T> success(T data) {
        return new NotificationResponse<>(HttpStatus.OK, "Success", data);
    }

    // Static factory method for error response
    public static <T> NotificationResponse<T> error(String message, HttpStatus status) {
        return new NotificationResponse<>(status, message, null);
    }
}
