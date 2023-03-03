package idorm.idormServer.fcm.domain;

import com.google.firebase.messaging.Message;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmMessage {

    @Id
    @Column(name = "fcm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean validate;


}
