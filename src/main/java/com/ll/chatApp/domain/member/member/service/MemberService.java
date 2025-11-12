package com.ll.chatApp.domain.member.member.service;

import com.ll.chatApp.domain.member.member.entity.Member;
import com.ll.chatApp.domain.member.member.repository.MemberRepository;
import com.ll.chatApp.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public RsData<Member> join(String username, String password) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 가입을 환영합니다".formatted(username), member);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}
