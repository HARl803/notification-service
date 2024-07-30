package com.haribo.notification_service.presentation.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotificationResponse<T> {

    private HttpStatus statusCode;
    private String resultMsg;
    private T resultData;

    // Default constructor
    public NotificationResponse() {}

    // Constructor for creating a new UserReportContentResponse
    public NotificationResponse(HttpStatus statusCode, String resultMsg, T resultData) {
        this.statusCode = statusCode;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }

    // Static factory method for success response
    public static <T> NotificationResponse<T> success(T data) {
        return new NotificationResponse<>(HttpStatus.OK, "Success", data);
    }

    // Static factory method for error response
    public static <T> NotificationResponse<T> error(String message, HttpStatus status) {
        return new NotificationResponse<>(status, message, null);
    }
}
