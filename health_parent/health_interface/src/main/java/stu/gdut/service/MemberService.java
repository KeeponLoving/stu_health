package stu.gdut.service;

import stu.gdut.domain.Member;

import java.util.List;

public interface MemberService {

    public Member findByTelephone(String telephone);

    public void add(Member member);

    public List<Integer> findMemberCountByMonth(List<String> month);
}
