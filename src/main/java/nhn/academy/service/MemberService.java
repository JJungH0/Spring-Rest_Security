package nhn.academy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhn.academy.InvalidPasswordException;
import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.model.exception.MemberAlreadyExistsException;
import nhn.academy.model.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String HASH_NAME = "Member:"; // Redis에서 사용할 Hash 키 이름
    @Autowired
    private ObjectMapper objectMapper;

    public void createMember(MemberCreateCommand memberCreateCommand) {
        // Redis에서 같은 ID의 회원이 이미 존재하는지 확인
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberCreateCommand.getId());
        // 존재 시 예외 발생
        if (o != null) {
            throw new MemberAlreadyExistsException("already used id");
        }

        Member member = new Member(memberCreateCommand.getId(), memberCreateCommand.getName(), memberCreateCommand.getAge(), memberCreateCommand.getClazz(), memberCreateCommand.getRole());
        // Redis의 Hash 자료구조에 [id : member] 형태로 저장
        redisTemplate.opsForHash().put(HASH_NAME, member.getId(), member);
    }

    public List<Member> getMembers() {
        // Redis에서 모든 회원 정보를 Map 형태로 가져옴
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);



        // 반환할 리스트 생성 (최적화를 위해 초기 크기 설정)
        List<Member> members = new ArrayList<>(entries.size());

        // HashMap의 값들 (Object -> Member)을 하나씩 꺼내 리스트에 추가
        for (Object value : entries.values()) {
            members.add((Member) value);
        }
        // 회원 리스트 반환
        return members;
    }

    public Member getMember(String memberId) {

        // ID로 Redis에서 객체 조회
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);

        Member member = objectMapper.convertValue(o, Member.class);
        if (o == null) {
            throw new MemberNotFoundException();
        }

        return new Member(memberId, member.getName(), member.getAge(), member.getClazz(), member.getRole());
    }

    public Member updateMember(String memberId) {
//        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);
//        if (Objects.isNull(o)) {
//            throw new MemberNotFoundException();
//        }
//
//        Member updateMember = new Member(memberId,
//                mcc.getName(),
//                mcc.getAge(),
//                mcc.getClazz(),
//                mcc.getRole());
//        redisTemplate.opsForHash().put(HASH_NAME, memberId, updateMember);
//        return updateMember;
        return null;
    }

    public Member login(MemberLoginRequest loginRequest) {
        Member member = getMember(loginRequest.getId());
        System.out.println(member);
//        if (!member.getPassword().equals(loginRequest.getPassword())) {
//            throw new InvalidPasswordException("Incorerect Password");
//        }
        return member;
    }


}
