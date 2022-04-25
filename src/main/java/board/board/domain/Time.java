package board.board.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass  //클래스가 만들어지지 않는 기초 클래스라는 의미
@EntityListeners(value = {AuditingEntityListener.class})  //엔티티의 변화를 감지하는 리스너
public abstract class Time {  //데이터 조작시 자동으로 날짜 수정 - Jpa audition 사용

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;
}
