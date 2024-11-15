package external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
    private static final String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";

    private final String accessKey;
    private final String secretKey;
    private final String regionString;

    // 생성자에서 AWS 자격 증명과 리전 문자열을 받아옵니다.
    public AwsConfig(@Value("${aws-property.access-key}") final String accessKey,
                     @Value("${aws-property.secret-key}") final String secretKey,
                     @Value("${aws-property.aws-region}") final String regionString) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionString = regionString;
    }

    // SystemPropertyCredentialsProvider 빈 생성
    @Bean
    public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
        // 시스템 속성에 AWS 자격 증명을 설정
        System.setProperty(AWS_ACCESS_KEY_ID, accessKey);
        System.setProperty(AWS_SECRET_ACCESS_KEY, secretKey);
        return SystemPropertyCredentialsProvider.create();
    }

    // AWS 리전 빈 생성
    @Bean
    public Region getRegion() {
        return Region.of(regionString);  // 설정된 리전 문자열로 Region 객체 생성
    }

    // S3Client 빈 생성
    @Bean
    /*테스트*/
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())  // 리전 설정
                .credentialsProvider(systemPropertyCredentialsProvider())  // 자격 증명 공급자 설정
                .build();
    }
}
