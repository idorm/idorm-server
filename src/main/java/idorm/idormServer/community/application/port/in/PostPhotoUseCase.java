package idorm.idormServer.community.application.port.in;

public interface PostPhotoUseCase {

	void save();

	void savePhoto();

	void delete();

	void findAllByPost();

	void findByIdAndPostId();
}
