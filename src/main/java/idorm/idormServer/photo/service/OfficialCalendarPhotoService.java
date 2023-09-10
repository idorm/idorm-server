package idorm.idormServer.photo.service;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.photo.domain.OfficialCalendarPhoto;
import idorm.idormServer.photo.repository.OfficialCalendarPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficialCalendarPhotoService {

    @Value("${s3.bucket-name.calendar-photo}")
    private String calendarPhotoBucketName;
    private final PhotoService photoService;
    private final OfficialCalendarPhotoRepository calendarPhotoRepository;

    /**
     * DB에 CalendarPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(OfficialCalendarPhoto photo) {
        try {
            calendarPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * CalendarPhoto 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(OfficialCalendarPhoto calendarPhoto) {
        try {
            calendarPhoto.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
