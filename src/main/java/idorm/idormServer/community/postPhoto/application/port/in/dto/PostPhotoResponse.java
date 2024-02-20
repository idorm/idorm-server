package idorm.idormServer.community.postPhoto.application.port.in.dto;

import java.util.List;

import idorm.idormServer.community.postPhoto.domain.PostPhoto;

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
