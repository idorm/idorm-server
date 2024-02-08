package idorm.idormServer.matchingMate.domain;

import static idorm.idormServer.common.exception.ExceptionCode.LIKEDMEMBER_NOT_FOUND;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Mates {
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Mate> mates = new HashSet<>();

    public static Mates empty() {
        return new Mates();
    }

    void addMate(Mate mate) {
        validateUnique(mate);
        mates.add(mate);
    }

    public void deleteMate(Member targetMember) {
        Set<Mate> deletedMates = mates.stream()
                .filter(mate -> !mate.isSameTarget(targetMember))
                .collect(Collectors.toSet());

        validateDeletedSize(this.mates.size(), deletedMates.size());
        mates.clear();
        mates.addAll(deletedMates);
    }

    void validateUnique(Mate mate) {
        if (mates.contains(mate)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_LIKED_MEMBER);
        }
    }

    void validateDeletedSize(int presentSize, int deletedSize) {
        if (presentSize == deletedSize) {
            throw new CustomException(null, LIKEDMEMBER_NOT_FOUND);
        }
    }
}