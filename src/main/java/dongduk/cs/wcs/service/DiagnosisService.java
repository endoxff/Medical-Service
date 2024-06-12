package dongduk.cs.wcs.service;

import dongduk.cs.wcs.domain.Diagnosis;
import dongduk.cs.wcs.domain.Doctor;
import dongduk.cs.wcs.domain.Member;
import dongduk.cs.wcs.domain.Patient;
import dongduk.cs.wcs.domain.enums.Type;
import dongduk.cs.wcs.dto.DiagnosisReqDto;
import dongduk.cs.wcs.dto.DiagnosisResDto;
import dongduk.cs.wcs.repository.DiagnosisRepository;
import dongduk.cs.wcs.repository.DoctorRepository;
import dongduk.cs.wcs.repository.MemberRepository;
import dongduk.cs.wcs.security.DigitalEnvelopeManager;
import dongduk.cs.wcs.security.DigitalSignatureManager;
import dongduk.cs.wcs.security.KeyPairManager;
import dongduk.cs.wcs.security.SecretKeyManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final MemberRepository memberRepository;
    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;

    // 진료 응답 Form을 위한 의사 목록
    @Transactional
    public List<Doctor> diagnosisRequestForm() {
        return doctorRepository.findAll();
    }

    // 진료 요청
    @Transactional
    public Diagnosis diagnosisRequest(String id, DiagnosisReqDto.DiagnosisRequestReqDto request) {
        // Sender Private Key 정보
        Member sender = memberRepository.getReferenceById(id);
        KeyPairManager keyPairManager = new KeyPairManager();
        PrivateKey privateKey = (PrivateKey) keyPairManager.load(id + "_private.key");

        // Digital Signature 생성
        DigitalSignatureManager digitalSignatureManager = new DigitalSignatureManager();
        byte[] signature = digitalSignatureManager.sign(privateKey, request.getSymptom());

        // Sender Secret Key 정보
        SecretKeyManager secretKeyManager = new SecretKeyManager();
        SecretKey secretKey = secretKeyManager.load(id + "_secret.key");

        // Receiver Public Key 정보
        Member receiver = memberRepository.getReferenceById(request.getReceiver());
        PublicKey publicKey = (PublicKey) keyPairManager.encode(receiver.getPublicKey());

        // Digital Envelope 생성
        DigitalEnvelopeManager digitalEnvelopeManager = new DigitalEnvelopeManager();
        byte[] encryptedData = digitalEnvelopeManager.encrypt(request.getSymptom().getBytes(), secretKey);
        byte[] encryptedSignature = digitalEnvelopeManager.encrypt(signature, secretKey);
        byte[] encryptedSecretKey = digitalEnvelopeManager.encrypt(secretKey.getEncoded(), publicKey);

        // DB에 Digital Envelope 저장
        Diagnosis diagnosis = new Diagnosis(sender, receiver, Type.REQUEST,
                encryptedData, encryptedSignature, encryptedSecretKey, LocalDateTime.now());

        return diagnosisRepository.save(diagnosis);
    }

    // 진료 요청/응답 확인을 위한 진료 정보
    @Transactional
    public List<Diagnosis> diagnosisRequestCheck(String id) {
        return diagnosisRepository.findByReceiverId(id);
    }

    // 진료 요청 상세 확인
    @Transactional
    public DiagnosisResDto.DiagnosisRequestResDto diagnosisRequestDetailCheck(Long id) {
        // 전자 봉투 정보
        Diagnosis diagnosis = diagnosisRepository.getReferenceById(id);

        // Receiver Private Key 정보
        KeyPairManager keyPairManager = new KeyPairManager();
        PrivateKey privateKey = (PrivateKey) keyPairManager.load(diagnosis.getReceiver().getId() + "_private.key");

        // Digital Envelope 복호화
        DigitalEnvelopeManager digitalEnvelopeManager = new DigitalEnvelopeManager();
        byte[] byteSecretKey = digitalEnvelopeManager.decrypt(diagnosis.getSecretKey(), privateKey);
        SecretKeyManager secretKeyManager = new SecretKeyManager();
        SecretKey secretKey = (SecretKey) secretKeyManager.encode(byteSecretKey);
        Arrays.fill(byteSecretKey, (byte)0);

        // Digital Envelope의 Data, Signature 복호화
        byte[] data = digitalEnvelopeManager.decrypt(diagnosis.getData(), secretKey);
        byte[] signature = digitalEnvelopeManager.decrypt(diagnosis.getSignature(), secretKey);
        PublicKey publicKey = (PublicKey) keyPairManager.encode(diagnosis.getSender().getPublicKey());

        // Signature 검증
        DigitalSignatureManager digitalSignatureManager = new DigitalSignatureManager();
        boolean verify = digitalSignatureManager.verify(data, signature, publicKey);
        Arrays.fill(signature, (byte)0);

        return new DiagnosisResDto.DiagnosisRequestResDto(diagnosis.getId(), diagnosis.getSender().getName(), new String(data), verify, diagnosis.getCreatedAt());
    }

    // 진료 응답 Form을 위한 Diagnosis 정보
    @Transactional
    public Map<String, String> diagnosisResponseForm(Long id) {
        Diagnosis diagnosis = diagnosisRepository.getReferenceById(id);

        Map<String, String> receiver = new HashMap<String, String>();
        receiver.put("id", diagnosis.getSender().getId());
        receiver.put("name", diagnosis.getSender().getName());

        return receiver;
    }

    // 진료 응답
    @Transactional
    public Diagnosis diagnosisResponse(String id, DiagnosisReqDto.DiagnosisResponseReqDto request) {
        // Sender Private Key 정보
        Member sender = memberRepository.getReferenceById(id);
        KeyPairManager keyPairManager = new KeyPairManager();
        PrivateKey privateKey = (PrivateKey) keyPairManager.load(id + "_private.key");

        // Digital Signature 생성
        DigitalSignatureManager digitalSignatureManager = new DigitalSignatureManager();
        byte[] signature = digitalSignatureManager.sign(privateKey, request.getPrescription());

        // Sender Secret Key 정보
        SecretKeyManager secretKeyManager = new SecretKeyManager();
        SecretKey secretKey = secretKeyManager.load(id + "_secret.key");

        // Receiver Public Key 정보
        Member receiver = memberRepository.getReferenceById(request.getReceiver());
        PublicKey publicKey = (PublicKey) keyPairManager.encode(receiver.getPublicKey());

        // Digital Envelope 생성
        DigitalEnvelopeManager digitalEnvelopeManager = new DigitalEnvelopeManager();
        byte[] encryptedData = digitalEnvelopeManager.encrypt(request.getPrescription().getBytes(), secretKey);
        byte[] encryptedSignature = digitalEnvelopeManager.encrypt(signature, secretKey);
        byte[] encryptedSecretKey = digitalEnvelopeManager.encrypt(secretKey.getEncoded(), publicKey);

        // DB에 Digital Envelope 저장
        Diagnosis diagnosis = new Diagnosis(sender, receiver, Type.RESPONSE,
                encryptedData, encryptedSignature, encryptedSecretKey, LocalDateTime.now());

        return diagnosisRepository.save(diagnosis);
    }
}
