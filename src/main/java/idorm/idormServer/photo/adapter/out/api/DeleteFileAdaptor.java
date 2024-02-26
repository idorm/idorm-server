package idorm.idormServer.photo.adapter.out.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.photo.adapter.out.api.exception.S3ClientException;
import idorm.idormServer.photo.application.port.out.DeleteFilePort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteFileAdaptor implements DeleteFilePort {

	private final AmazonS3Client amazonS3Client;

	@Value("${s3.bucket-name.member-photo}")
	private String memberPhotoBucketName;

	@Value("${s3.bucket-name.post-photo}")
	private String postPhotoBucketName;

	@Override
	public void deleteMemberPhotoFile(final Member member) {
		deleteFileFromS3(memberPhotoBucketName, member.getProfilePhotoUrl());
	}

	@Override
	public void deletePostPhotoFiles(final Post post) {
		post.getPostPhotos().forEach(postPhoto -> deleteFileFromS3(postPhotoBucketName, postPhoto.getPhotoUrl()));
	}

	private void deleteFileFromS3(String bucketname, String fileUrl) {
		String[] splitUrl = fileUrl.split("/");
		String fileName = splitUrl[splitUrl.length - 2] + "/" + splitUrl[splitUrl.length - 1];

		try {
			amazonS3Client.deleteObject(bucketname, fileName);
		} catch (SdkClientException e) {
			throw new S3ClientException();
		}
	}
}
