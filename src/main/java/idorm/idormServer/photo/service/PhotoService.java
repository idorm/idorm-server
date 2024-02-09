package idorm.idormServer.photo.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import idorm.idormServer.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static idorm.idormServer.common.exception.ExceptionCode.*;

// util? interface화?

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final AmazonS3Client amazonS3Client;

    public void validateFileType(MultipartFile file) {
        String type = file.getContentType().split("/")[1];

        if (!type.equals("jpg") && !type.equals("jpeg") && !type.equals("png")) {
            throw new CustomException(null, FILE_TYPE_UNSUPPORTED);
        }
    }

    public void validateFileExistence(MultipartFile file) {
        if (file == null)
            throw new CustomException(null, FILE_NOT_FOUND);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertingFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertingFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertingFile;
    }

    public String insertFileToS3(String bucketName, String folderName, String fileName, MultipartFile file) {

        try {
            File convertedFile = convertMultiPartToFile(file);
            String uploadingFileName = folderName + "/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadingFileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            String url = amazonS3Client.getUrl(bucketName, uploadingFileName).toString();
            return url;
        } catch (IOException e) {
            throw new CustomException(e, S3_SERVER_ERROR);
        }
    }

    public void deleteFileFromS3(String bucketname, String filePath) {;
        try {
            amazonS3Client.deleteObject(bucketname, filePath);
        } catch (SdkClientException e) {
            throw new CustomException(e, S3_SERVER_ERROR);
        }
    }
}
