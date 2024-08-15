package com.haribo.notification_service.presentation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode{

    NOTIFICATION_NOT_SENT(HttpStatus.BAD_REQUEST, "알림 전송에 실패하였습니다."),
    OBJECT_TO_JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST, "JSON 파싱에 실패하였습니다."),
    NOTIFICATION_NOT_SAVED(HttpStatus.BAD_REQUEST, "알림 저장에 실패하였습니다."),
    JSON_TO_OBJECT_PARSING_FAILED(HttpStatus.BAD_REQUEST, "Object 파싱에 실패하였습니다 : Failed to parse JSON message"),
    READING_ROOT_NODE_FAILED(HttpStatus.BAD_REQUEST, "Json 데이터의 root node를 인식할 수 없습니다 : Failed to parse JSON from auth service"),
    READING_PROFILE_MEMBER_NODE_FAILED(HttpStatus.BAD_REQUEST, "Json 데이터의 profile member node를 인식할 수 없습니다."),
    PARSING_ERROR_OCCURED(HttpStatus.BAD_REQUEST, "부적합한 ZonedDateTime 형식입니다."),
    GENERIC_ERROR(HttpStatus.BAD_REQUEST, "메세지를 가져오는 과정에 문제가 발생했습니다."),
    NOTIFICATION_COLLECTION_IS_EMPTY(HttpStatus.BAD_REQUEST, "새로운 알림이 없습니다."),
    NOTIFICATION_TYPE_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 알람 타입 입니다."),
    MARK_AS_READ_FAILED(HttpStatus.BAD_REQUEST, "알림 읽음 표시 실패"),
    NOTIFICATION_COLLECTION_ETC_IS_EMPTY(HttpStatus.BAD_REQUEST, "ETC 알림 리스트 파싱 실패"),
    NOTIFICATION_COLLECTION_COMPTER_CHAT_IS_EMPTY(HttpStatus.BAD_REQUEST, "ETC 알림 리스트 파싱 실패"),
    DUPLICATE_KEY(HttpStatus.CONFLICT, "알림의 ID가 중복되어 저장에 실패하였습니다."),
    DATABASE_ERROR(HttpStatus.BAD_REQUEST, "DB 저장소 접근에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효한 사용자가 아닙니다. 로그인 정보를 가져오는데 실패하였습니다."),
    MENTOR_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "멘토의 정보를 찾을 수 없습니다."),
    MENTEE_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "멘티의 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "예약 정보를 찾을 수 없습니다."),
    POSSIBLE_START_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "컴터챗 요청 시간을 찾을 수 없습니다."),
    PROFILE_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "profile ID에 해당하는 닉네임을 찾을 수 없습니다."),
    MENTEE_AVAILABLE_TIMES_IS_NULL(HttpStatus.BAD_REQUEST, "멘티가 제안한 시간이 없습니다."),
    LOG_OBJECT_IS_NULL(HttpStatus.BAD_REQUEST, "예약 ID에 해당하는 로그가 없습니다."),
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
