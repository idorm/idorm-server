package idorm.idormServer.calendar.domain;

import idorm.idormServer.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepoverCalendar {

    @Id
    @Column(name = "sleepover_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LocalDate으로 저장? 저장 방식에 따라 쿼리 작성, 조회 속도가 달라질듯
    @Embedded
    private Period period;

    // participant는 어떻게 저장? TeamSchedule targets랑 같이 활용?
    @OneToOne
    @Column(nullable = false, name = "target_member_id")
    private Member targetMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
