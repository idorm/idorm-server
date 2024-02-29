package idorm.idormServer.photo.application.port.out;

import java.util.List;

public interface DeleteFilePort {

	void deleteMemberPhotoFile(String fileUrl);

	void deletePostPhotoFiles(List<String> fileUrls);
}