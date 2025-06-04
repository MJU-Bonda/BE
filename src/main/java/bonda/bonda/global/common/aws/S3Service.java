package bonda.bonda.global.common.aws;

import bonda.bonda.global.exception.BusinessException;
import bonda.bonda.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;
    @Value("${cloud.aws.s3.region}")
    private String REGION;

    private final AmazonS3 amazonS3;

    @Transactional
    public String uploadImage(String FolderName, MultipartFile image) {
        // 이미지 존재 여부 확인
        if(image == null){
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        // {폴더명}/{사진이름}-{uuid}.{확장자} 로 변환
        String saveFileName = convertToSaveName(FolderName, image.getOriginalFilename());

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        try (InputStream inputStream = image.getInputStream()) {
            // S3에 업로드 및 저장
            amazonS3.putObject(new PutObjectRequest(BUCKET, saveFileName, inputStream, metadata));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return getUrl(saveFileName); //https:my..... 형식으로 반환
    }

    private String convertToSaveName(String folderName, String originalFilename) {
        int idx = originalFilename.lastIndexOf('.'); //마지막 . 인덱스 추출
        if (idx < 0) {
            throw new IllegalArgumentException("잘못된 파일명: 확장자가 없습니다.");
        }
        String name = originalFilename.substring(0, idx);   // "photo"
        String ext  = originalFilename.substring(idx);      // ".jpg"

        return String.format("%s/%s-%s%s", folderName, name, UUID.randomUUID(), ext);
    }

    public String getUrl(String s3key) { //s3key == uploadImage 메소드의 savedFileName과 동일
        if(s3key == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");
        }
        return amazonS3.getUrl(BUCKET, s3key).toString();
    }



    @Transactional
    public void deleteImage(String url) {
        String s3Key = extractS3KeyFromUrl(url);
        if (s3Key.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_DELETE_FAILED);
        }
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(BUCKET, s3Key));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    private String extractS3KeyFromUrl(String url) {
        try {
            // URL에서 버킷 경로를 제거하고 S3 Key 부분만 남김
            String keyWithEncoding = url.replace("https://" + BUCKET + ".s3." + REGION + ".amazonaws.com/", "");
            // URL 디코딩
            return URLDecoder.decode(keyWithEncoding, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FILE_DELETE_FAILED);
        }
    }
}