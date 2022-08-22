package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

    @Id
    @GeneratedValue
    @Column(name="photo_id")
    private Long id;

    private String uploadFileName; // 업로드 파일명
    private String storeFileName; // 저장된 파일명

    @OneToOne(mappedBy = "photo")
    private Member member; // 프로필 사진 매핑을 위한 멤버

}
