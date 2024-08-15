package com.haribo.notification_service.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "auth_member")
public class AuthMember {
    @Id
    @Column(name = "member_uid", length = 48)
    private String memberUid;

    @Column(name = "kakao_uid", nullable = false, unique = true)
    private Long kakaoUid;

    @Setter
    @Column(name = "last_login_date", nullable = false)
    private LocalDateTime lastLoginDate;

    @OneToOne(mappedBy = "authMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonManagedReference
    private ProfileMember profileMember;
}
