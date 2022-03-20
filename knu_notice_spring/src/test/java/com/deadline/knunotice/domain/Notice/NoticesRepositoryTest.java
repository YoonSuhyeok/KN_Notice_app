package com.deadline.knunotice.domain.Notice;

import com.deadline.knunotice.domain.Notices;
import com.deadline.knunotice.domain.NoticesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NoticesRepositoryTest {

    @Autowired
    private NoticesRepository noticesRepository;

    @Test
    void 공지글_저장_불러오기(){
        //given
        String college = "IT";
        String major = "컴퓨터공학";
        Notices notices = Notices.builder().college(college).major(major).build();
        noticesRepository.save(notices);

        //when
        List<Notices> noticesList = noticesRepository.findAll();

        //then
        Notices notice = noticesList.get(0);
        assertThat(notice.getCollege()).isEqualTo(college);
        assertThat(notice.getMajor()).isEqualTo(major);
    }
}
