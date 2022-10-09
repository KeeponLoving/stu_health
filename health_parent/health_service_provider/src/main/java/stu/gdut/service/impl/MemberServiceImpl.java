package stu.gdut.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.dao.MemberMapper;
import stu.gdut.domain.Member;
import stu.gdut.service.MemberService;
import stu.gdut.utils.MD5Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 会员服务
 */
@DubboService(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if(password!=null){
            // 使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberMapper.add(member);
    }

    // 根据月份查询会员数量
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        Calendar cale = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        for (String month : months) {
            try {
                cale.setTime(formatter.parse(month+".1"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 获得当前月的最后一天
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            String lastDayOfMonth = formatter.format(cale.getTime());
            Integer count = memberMapper.findMemberCountBeforeDate(lastDayOfMonth);
            memberCount.add(count);
        }
        return memberCount;
    }
}
