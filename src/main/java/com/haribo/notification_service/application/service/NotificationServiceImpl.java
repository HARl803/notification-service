package com.haribo.notification_service.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haribo.notification_service.application.dto.MessageDtoWithLink;
import com.haribo.notification_service.application.dto.MessageDtoWithMatchingId;
import com.haribo.notification_service.domain.MessageForCompterChat;
import com.haribo.notification_service.domain.MessageForEtc;
import com.haribo.notification_service.domain.ProfileMember;
import com.haribo.notification_service.domain.ReservationDocs;
import com.haribo.notification_service.domain.repository.ProfileMemberRepository;
import com.haribo.notification_service.presentation.exception.CustomErrorCode;
import com.haribo.notification_service.presentation.exception.CustomException;
import com.haribo.notification_service.presentation.request.NotificationRequest;
import com.haribo.notification_service.presentation.response.NotificationResponseForCompterChat;
import com.haribo.notification_service.presentation.response.NotificationResponseForEtc;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final ProfileMemberRepository profileMemberRepository;
    private final MongoTemplate mongoTemplate;

    @Value("${path.to.auth}")
    private String PATH_TO_AUTH;

    @Override
    public LinkedHashMap<String, String> authorizedProfileId(String sessionId) throws URISyntaxException {

        log.info("profileId 알아보기!: session check!");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JSESSIONID = " + sessionId);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(new URI(PATH_TO_AUTH), HttpMethod.GET, entity, LinkedHashMap.class);

        LinkedHashMap<String, LinkedHashMap<String, String>> map =  (LinkedHashMap<String, LinkedHashMap<String, String>>) responseEntity.getBody();

        log.info("profileId: {}, nickName: {}", map.get("profileMember").get("profileId"), map.get("profileMember").get("nickName"));

        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("profileId", map.get("profileMember").get("profileId"));
        response.put("nickName", map.get("profileMember").get("nickName"));

        return response;
    }

    @Override
    public List<NotificationResponseForEtc> getMessageListByReceiverFromMongodbForEtc(String receiver) {

        log.info("서비스의 Etc리스트 메소드 접근");
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(receiver))
                .addCriteria(Criteria.where("typeId").in("NT05", "NT06"));

        List<MessageForEtc> dtoList = mongoTemplate.find(query, MessageForEtc.class);
        log.info("mongoTemplate으로 dtoList 확인 성공");

        if(dtoList.isEmpty()) throw new CustomException(CustomErrorCode.NOTIFICATION_COLLECTION_IS_EMPTY);

        List<NotificationResponseForEtc> updatedMessages = new ArrayList<>();

        log.info("dtoList로 정제된 Etc 리스트 생성 시작");
        for (MessageForEtc messageForEtc : dtoList) {
            try {
                ZonedDateTime seoulTime = messageForEtc.getCreatedTimeAsZonedDateTime()
                        .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

                NotificationResponseForEtc response = NotificationResponseForEtc.builder()
                        .notiId(messageForEtc.getNotiId())
                        .receiver(messageForEtc.getReceiver())
                        .messageContent(messageForEtc.getMessageContent())
                        .createdTime(seoulTime.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES))
                        .isRead(messageForEtc.isRead())
                        .link(messageForEtc.getLink())
                        .typeId(getDescOfTypeId(messageForEtc.getTypeId()))
                        .build();

                updatedMessages.add(response);
                log.info("notiId : {} 성공", response.getNotiId());
            } catch (Exception e) {
                log.error("Failed to process message from MongoDB: {}", e.getMessage());
                throw new CustomException(CustomErrorCode.NOTIFICATION_COLLECTION_ETC_IS_EMPTY);
            }
        }

        log.info("Etc 리스트 생성 성공");
        return updatedMessages;
    }

    @Override
    public List<NotificationResponseForCompterChat> getMessageListByReceiverFromMongodbForCompterChat(String receiver) {

        log.info("서비스의 CompterChat 리스트 메소드 접근");
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(receiver))
                .addCriteria(Criteria.where("typeId").in("NT01", "NT02", "NT03", "NT04"));

        List<MessageForCompterChat> dtoList = mongoTemplate.find(query, MessageForCompterChat.class);
        log.info("mongoTemplate으로 dtoList 확인 성공");

        if(dtoList.isEmpty()) throw new CustomException(CustomErrorCode.NOTIFICATION_COLLECTION_IS_EMPTY);

        List<NotificationResponseForCompterChat> updatedMessages = new ArrayList<>();

        log.info("dtoList로 정제된 CompterChat 리스트 생성 시작");
        for (MessageForCompterChat messageForCompterChat : dtoList) {
            try {
                ZonedDateTime seoulTime = messageForCompterChat.getCreatedTimeAsZonedDateTime()
                        .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

                NotificationResponseForCompterChat response = NotificationResponseForCompterChat.builder()
                        .notiId(messageForCompterChat.getNotiId())
                        .receiver(messageForCompterChat.getReceiver())
                        .messageContent(messageForCompterChat.getMessageContent())
                        .createdTime(seoulTime.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES))
                        .isRead(messageForCompterChat.isRead())
                        .matchingId(messageForCompterChat.getMatchingId())
                        .typeId(getDescOfTypeId(messageForCompterChat.getTypeId()))
                        .build();

                updatedMessages.add(response);
                log.info("notiId : {} 성공", response.getNotiId());
            } catch (Exception e) {
                log.error("Failed to process message from MongoDB: {}", e.getMessage());
                throw new CustomException(CustomErrorCode.NOTIFICATION_COLLECTION_COMPTER_CHAT_IS_EMPTY);
            }
        }

        log.info("CompterChat 리스트 생성 성공");
        return updatedMessages;
    }

    @Transactional
    @Override
    public void saveNotificationWithMatchingId(NotificationRequest request) {

        log.info("서비스의 CompterChat 알림 저장 메소드 접근");
        MessageDtoWithMatchingId dto = MessageDtoWithMatchingId.builder()
                .notiId(generatePrimaryKeyForNoti())
                .receiver(request.receiver())
                .messageContent(notificationContentDivider(request.typeId(), request.add()))
                .createdTime(ZonedDateTime.now().toString())
                .isRead(false)
                .matchingId(request.add())
                .typeId(request.typeId())
                .build();

        try {
            mongoTemplate.insert(dto);
        } catch (DuplicateKeyException e) {
            throw new CustomException(CustomErrorCode.DUPLICATE_KEY);
        } catch (DataAccessException e) {
            throw new CustomException(CustomErrorCode.DATABASE_ERROR);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.GENERIC_ERROR);
        }
        log.info("서비스의 CompterChat 알림 저장 성공");
    }

    @Transactional
    @Override
    public void saveNotificationForCscenter(NotificationRequest request) {

        log.info("서비스의 신고.문의 알림 저장 메소드 접근");
        MessageDtoWithLink dto;
        if(request.typeId().equals("NT05")) {
            dto = MessageDtoWithLink.builder()
                .notiId(generatePrimaryKeyForNoti())
                .receiver(request.receiver())
                .messageContent(request.content())
                .createdTime(ZonedDateTime.now().toString())
                .isRead(false)
                .link(request.add())
                .typeId(request.typeId())
                .build();
        }
        else throw new CustomException(CustomErrorCode.NOTIFICATION_TYPE_ID_NOT_FOUND);

        try {
            mongoTemplate.insert(dto);
        } catch (DuplicateKeyException e) {
            throw new CustomException(CustomErrorCode.DUPLICATE_KEY);
        } catch (DataAccessException e) {
            throw new CustomException(CustomErrorCode.DATABASE_ERROR);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.GENERIC_ERROR);
        }
        log.info("서비스의 신고.문의 알림 저장 성공");
    }

    @Transactional
    @Override
    public void saveNotificationForCommunity(NotificationRequest request) {

        log.info("서비스의 게시글 댓글 생성 시의 알림 저장 메소드 접근");
        MessageDtoWithLink dto;
        if(request.typeId().equals("NT06")) {
            dto = MessageDtoWithLink.builder()
                    .notiId(generatePrimaryKeyForNoti())
                    .receiver(request.receiver())
                    .messageContent("등록하신 게시글에 댓글이 달렸습니다. 지금 확인해 보세요!")
                    .createdTime(ZonedDateTime.now().toString())
                    .isRead(false)
                    .link(request.add())
                    .typeId(request.typeId())
                    .build();
        }
        else throw new CustomException(CustomErrorCode.NOTIFICATION_TYPE_ID_NOT_FOUND);

        try {
            mongoTemplate.insert(dto);
        } catch (DuplicateKeyException e) {
            throw new CustomException(CustomErrorCode.DUPLICATE_KEY);
        } catch (DataAccessException e) {
            throw new CustomException(CustomErrorCode.DATABASE_ERROR);
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.GENERIC_ERROR);
        }
        log.info("서비스의 게시글 댓글 생성 시의 알림 저장 성공");
    }

    @Override
    @Transactional
    public void markAsRead(String notiId) {

        log.info("서비스의 알림 읽음 표시 메소드 접근");
        Query query = new Query(Criteria.where("notiId").is(notiId));
        Update update = new Update().set("isRead", true);

        try {
            mongoTemplate.updateFirst(query, update, MessageForEtc.class);
        } catch (Exception e) {
            log.info("읽음 표시 실패! 에러 메시지: {}", e.getMessage());
            throw new CustomException(CustomErrorCode.MARK_AS_READ_FAILED);
        }
        log.info("읽음 표시 성공");
    }

    @Override
    public String getDescOfTypeId(String typeId) {

        log.info("서비스의 알림 타입 아이디에 해당하는 한국어 단어 리턴 메소드 접근");
        return switch (typeId){
            case "NT01" -> "컴터챗 확정";
            case "NT02" -> "컴터챗 요청";
            case "NT03" -> "컴터챗 결제";
            case "NT04" -> "컴터챗 거절";
            case "NT05" -> "기타";
            case "NT06" -> "게시글";
            default -> throw new CustomException(CustomErrorCode.NOTIFICATION_TYPE_ID_NOT_FOUND);
        };
    }

    @Override
    public String generatePrimaryKeyForNoti() {

        log.info("서비스의 알림 아이디 생성 메소드 접근");
        String prefix = "NOTI";
        UUID uuid = UUID.randomUUID();
        return prefix + uuid;
    }

    @Override
    public String notificationContentDivider(String typeId, String add) {

        log.info("서비스의 알림 내용 생성 및 분기 메소드 접근");
        return switch (typeId) {
            case "NT01" -> findMentorId(add) + "님과의 컴터챗이 확정되었습니다."; //"컴터챗 확정"
            case "NT02" -> {
                String time = findCompterChatRequestedTime(add);
                yield findMenteeId(add) + "님이 " + time.substring(0, 10) + " " + time.substring(11, 16) + "에 시작하는 컴터챗(30분)을 요청하였습니다.";
            }
            case "NT03" -> findMentorId(add) + "님과의 컴터챗이 매칭되었습니다. 결제를 진행하세요."; //"컴터챗 결제"
            case "NT04" -> findMentorId(add) + "님이 요청을 거절하였습니다. 컴터챗이 취소되었습니다."; //"컴터챗 거절"
            default -> throw new CustomException(CustomErrorCode.NOTIFICATION_TYPE_ID_NOT_FOUND);
        };
    }

    @Override
    public String findMenteeId(String add) {

        log.info("서비스의 알림 내용 중 멘티 아이디 찾는 메소드 접근");
        int matchingId = Integer.parseInt(add);

        Query query = new Query();
        query.addCriteria(Criteria.where("reservationId").is(matchingId));

        Map<String, ReservationDocs.MenteeAvailableTimes> menteeAvailableTimes;
        try {
            ReservationDocs reservation = mongoTemplate.findOne(query, ReservationDocs.class);
            log.info("예약 아이디에 해당하는 객체 찾음");

            menteeAvailableTimes = reservation.getMenteeAvailableTimes();
            if(menteeAvailableTimes == null) {
                log.info("그러나 멘티가 제시한 시간은 존재하지 않음");
                throw new CustomException(CustomErrorCode.MENTEE_AVAILABLE_TIMES_IS_NULL);
            }
        } catch (RuntimeException e) {
            log.info("입력된 예약 아이디는 존재하지 않음");
            throw new CustomException(CustomErrorCode.RESERVATION_NOT_FOUND);
        }

        Iterator<String> keys = menteeAvailableTimes.keySet().iterator();
        String menteeId = null;
        while(keys.hasNext()) {
            String key = keys.next();
            menteeId = menteeAvailableTimes.get(key).getMenteeId();
        }

        if(menteeId != null) {
            ProfileMember member = profileMemberRepository.findById(menteeId)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.PROFILE_MEMBER_NOT_FOUND));
            log.info("프로필 멤버의 닉네임 찾음");
            return member.getNickName();
        }
        else {
            log.info("멘티 아이디 존재하지 않음");
            throw new CustomException(CustomErrorCode.MENTEE_ID_NOT_FOUND);
        }
    }

    @Override
    public String findMentorId(String add) {

        log.info("서비스의 알림 내용 중 멘토 아이디 찾는 메소드 접근");
        int matchingId = Integer.parseInt(add);

        Query query = new Query();
        query.addCriteria(Criteria.where("reservation_id").is(matchingId));

        Map<String, ReservationDocs.Log> logObject;
        try {
            ReservationDocs reservation = mongoTemplate.findOne(query, ReservationDocs.class);
            log.info("예약 아이디에 해당하는 객체 찾음");

            logObject = reservation.getLog();
            if(logObject == null) {
                log.info("그러나 해당 예약 건에 대한 로그는 존재하지 않음");
                throw new CustomException(CustomErrorCode.LOG_OBJECT_IS_NULL);
            }
        } catch (RuntimeException e) {
            log.info("입력된 예약 아이디는 존재하지 않음");
            throw new CustomException(CustomErrorCode.RESERVATION_NOT_FOUND);
        }

        Iterator<String> keys = logObject.keySet().iterator();
        String mentorId = null;
        while(keys.hasNext()) {
            String key = keys.next();
            mentorId = logObject.get(key).getMemberId();
        }

        if(mentorId != null) {
            ProfileMember member = profileMemberRepository.findById(mentorId)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.PROFILE_MEMBER_NOT_FOUND));
            log.info("프로필 멤버의 닉네임 찾음");
            return member.getNickName();
        }
        else {
            log.info("멘토 아이디 존재하지 않음");
            throw new CustomException(CustomErrorCode.MENTOR_ID_NOT_FOUND);
        }
    }

    @Override
    public String findCompterChatRequestedTime(String add) {

        log.info("서비스의 알림 내용 중 멘티 요청 시간 찾는 메소드 접근");
        int matchingId = Integer.parseInt(add);
        Query query = new Query();
        query.addCriteria(Criteria.where("reservationId").is(matchingId));

        Map<String, ReservationDocs.MenteeAvailableTimes> menteeAvailableTimes;
        try {
            ReservationDocs reservation = mongoTemplate.findOne(query, ReservationDocs.class);
            log.info("예약 아이디에 해당하는 객체 찾음");

            menteeAvailableTimes = reservation.getMenteeAvailableTimes();
            if(reservation.getMenteeAvailableTimes() == null) {
                log.info("그러나 멘티가 제시한 시간은 존재하지 않음");
                throw new CustomException(CustomErrorCode.MENTEE_AVAILABLE_TIMES_IS_NULL);
            }
        } catch (RuntimeException e) {
            log.info("입력된 예약 아이디는 존재하지 않음");
            throw new CustomException(CustomErrorCode.RESERVATION_NOT_FOUND);
        }

        Iterator<String> keys = menteeAvailableTimes.keySet().iterator();
        String possibleStartTimes = null;
        while(keys.hasNext()) {
            String key = keys.next();
            possibleStartTimes = menteeAvailableTimes.get(key).getPossibleStartTimes().getFirst();
        }

        if(possibleStartTimes != null) {
            log.info("멘티 요청 시간 찾음");
            return possibleStartTimes;
        }
        else {
            log.info("멘티 요청 시간 존재하지 않음");
            throw new CustomException(CustomErrorCode.POSSIBLE_START_TIME_NOT_FOUND);
        }
    }
}
