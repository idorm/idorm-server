package idorm.idormServer.community.postPhoto.application.port.in.dto;

import idorm.idormServer.community.postPhoto.entity.PostPhoto;

import java.util.List;

public record PostPhotoResponse(
	Long photoId,
	String photoUrl
) {
	public static List<PostPhotoResponse> of(final List<PostPhoto> postPhotos) {
		List<PostPhotoResponse> responses = postPhotos.stream()
			.map(postPhoto -> new PostPhotoResponse(postPhoto.getId(), postPhoto.getPhotoUrl()))
			.toList();
		return responses;
	}
}