package com.fastcampus.aptner.member.service;

import com.fastcampus.aptner.member.domain.Member;

import java.util.Optional;

public interface LoginMemberService {

    Member findByUsername(String username);
    Optional<Member> findMemberById(Long memberId);

}
