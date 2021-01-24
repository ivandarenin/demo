package ru.diasoft.micro.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.micro.DemoApplication;
import ru.diasoft.micro.domain.SmsVerification;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class SmsVerificationRepositoryTest {

    public static final String PHONE_NUMBER = "4852536913";
    public static final String SECRET_CODE = "123";
    public static final String STATUS = "WAITING";
    public static final String PROCESS_GUID = "12345";

    @Autowired
    private SmsVerificationRepository smsVerificationRepository;

    @Test
    public void smsVerificationCreateTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber(PHONE_NUMBER)
                .secretCode(SECRET_CODE)
                .status(STATUS)
                .build();

        SmsVerification smsVerificationCreated = smsVerificationRepository.save(smsVerification);
        assertThat(smsVerification).isEqualToComparingOnlyGivenFields(smsVerificationCreated, "verificationId");
        assertThat(smsVerificationCreated.getVerificationId()).isNotNull();
    }

    @Test
    public void findBySecretCodeAndProcessGuidAndStatus() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(PROCESS_GUID)
                .phoneNumber(PHONE_NUMBER)
                .secretCode(SECRET_CODE)
                .status(STATUS)
                .build();

        SmsVerification smsVerificationCreated = smsVerificationRepository.save(smsVerification);
        assertThat(smsVerificationRepository.findBySecretCodeAndProcessGuidAndStatus(SECRET_CODE, PROCESS_GUID, STATUS)
                .orElse(SmsVerification.builder().build())).isEqualTo(smsVerificationCreated);

        assertThat(smsVerificationRepository.findBySecretCodeAndProcessGuidAndStatus("wrong", PROCESS_GUID, STATUS))
                .isEmpty();
    }
}
