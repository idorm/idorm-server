package idorm.idormServer.matchingMate.domain;

import static idorm.idormServer.common.exception.ExceptionCode.DISLIKEDMEMBER_NOT_FOUND;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import javax.persistence.Embeddable;

@Embeddable
public class NonFavoriteMates extends Mates {

    @Override
    void validateUnique(Mate mate) {
        if (getMates().contains(mate)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_DISLIKED_MEMBER);
        }
    }

    @Override
    void validateDeletedSize(int presentSize, int deletedSize) {
        if (presentSize == deletedSize) {
            throw new CustomException(null, DISLIKEDMEMBER_NOT_FOUND);
        }
    }
}