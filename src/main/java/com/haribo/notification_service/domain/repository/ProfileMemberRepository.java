package com.haribo.notification_service.domain.repository;

import com.haribo.notification_service.domain.ProfileMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileMemberRepository extends JpaRepository<ProfileMember, String> { }