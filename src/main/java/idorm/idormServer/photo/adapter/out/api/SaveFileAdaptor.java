package idorm.idormServer.photo.adapter.out.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.photo.adapter.out.api.exception.NotFoundFileException;
import idorm.idormServer.photo.adapter.out.api.exception.S3ClientException;
import idorm.idormServer.photo.adapter.out.api.exception.UnsupportedFileTypeException;
import idorm.idormServer.photo.application.port.out.SaveFilePort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveFileAdaptor implements SaveFilePort {

	private static final Set<String> VALID_FILE_TYPES = Set.of("jpg", "jpeg", "png");

	private final AmazonS3Client amazonS3Client;

	@Value("${s3.bucket-name.member-photo}")
	private String memberPhotoBucketName;

	@Value("${s3.bucket-name.post-photo}")
	private String postPhotoBucketName;

	@Override
	public String saveMemberPhotoFile(final Member member, final MultipartFile multipartFile) {
		String fileName = member.getId() + "/" + UUID.randomUUID() + getFileType(multipartFile);
		File file = convertMultiPartToFile(multipartFile);

		return insertMemberPhotoFileToS3(fileName, file);
	}

	@Override
	public List<String> savePostPhotoFiles(final Post post, final List<MultipartFile> multipartFiles) {
		return insertPostPhotosFileToS3(post.getId(), multipartFiles);
	}

	private String insertMemberPhotoFileToS3(String fileName, File file) {
		amazonS3Client.putObject(new PutObjectRequest(memberPhotoBucketName, fileName, file)
			.withCannedAcl(CannedAccessControlList.PublicRead));
		file.delete();

		return amazonS3Client.getUrl(memberPhotoBucketName, fileName).toString();
	}

	private List<String> insertPostPhotosFileToS3(Long postId, List<MultipartFile> multipartFiles) {
		List<String> postPhotoUrls = multipartFiles.stream()
			.map(multipartFile -> {
				String fileName = postId + "/" + UUID.randomUUID() + getFileType(multipartFile);
				File file = convertMultiPartToFile(multipartFile);

				amazonS3Client.putObject(new PutObjectRequest(postPhotoBucketName, fileName, file)
					.withCannedAcl(CannedAccessControlList.PublicRead));
				file.delete();

				return amazonS3Client.getUrl(postPhotoBucketName, fileName).toString();
			})
			.toList();

		return postPhotoUrls;
	}

	private File convertMultiPartToFile(MultipartFile multipartFile) {
		try {
			File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(multipartFile.getBytes());
			fileOutputStream.close();

			return file;
		} catch (IOException exception) {
			throw new S3ClientException();
		}
	}

	private String getFileType(MultipartFile file) {
		if (file == null) {
			throw new NotFoundFileException();
		}

		String type = file.getContentType().split("/")[1];
		if (!VALID_FILE_TYPES.contains(type)) {
			throw new UnsupportedFileTypeException();
		}
		return type;
	}
}
