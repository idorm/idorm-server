package idorm.idormServer.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용, 부모 클래스를 상속받는 자식 클래스에 매핑
@EntityListeners(AuditingEntityListener.class) // Auditing 적용 어노테이션
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성일

}