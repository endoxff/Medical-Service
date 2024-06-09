package dongduk.cs.wcs.service;

import dongduk.cs.wcs.domain.Doctor;
import dongduk.cs.wcs.domain.Member;
import dongduk.cs.wcs.domain.Patient;
import dongduk.cs.wcs.domain.enums.Gender;
import dongduk.cs.wcs.domain.enums.Subject;
import dongduk.cs.wcs.dto.MemberReqDto;
import dongduk.cs.wcs.repository.DoctorRepository;
import dongduk.cs.wcs.repository.MemberRepository;
import dongduk.cs.wcs.repository.PatientRepository;
import dongduk.cs.wcs.security.KeyPairManager;
import dongduk.cs.wcs.security.SecretKeyManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    /* 환자 회원 가입 */
    @Transactional
    public Patient patientSignup(MemberReqDto.PatientSignupReqDto request) throws NoSuchAlgorithmException {
        // 아이디 중복 확인
        if (!checkId(request.getId())) {
            throw new RuntimeException("해당 아이디는 이미 존재합니다.");
        }

        // Secret Key 생성 및 저장
        SecretKeyManager secretKeyManager = new SecretKeyManager();
        String secretKeyFileName = request.getId() + "_secret.key";
        secretKeyManager.create();
        secretKeyManager.save(secretKeyFileName);

        // Public Key & Private Key 생성 및 저장
        KeyPairManager keyPairManager = new KeyPairManager();
        String privateKeyFileName = request.getId() + "_public.key";
        keyPairManager.create();
        keyPairManager.save(privateKeyFileName);

        // Patient DB에 저장
        Gender gender = null;

        switch (request.getGender()) {
            case 1:
                gender = Gender.MALE;
                break;
            case 2:
                gender = Gender.FEMALE;
                break;
        }

        Patient patient = new Patient(request.getId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                keyPairManager.getPublicKey().getEncoded(),
                request.getBirthday(),
                gender);

        patientRepository.save(patient);

        return patient;
    }

    /* 의사 회원 가입 */
    @Transactional
    public Doctor doctorSignup(MemberReqDto.DoctorSignupReqDto request) throws NoSuchAlgorithmException {
        // 아이디 중복 확인
        if (!checkId(request.getId())) {
            throw new RuntimeException("해당 아이디는 이미 존재합니다.");
        }

        // Secret Key 생성 및 저장
        SecretKeyManager secretKeyManager = new SecretKeyManager();
        String secretKeyFileName = request.getId() + "_secret.key";
        secretKeyManager.create();
        secretKeyManager.save(secretKeyFileName);

        // Public Key & Private Key 생성 및 저장
        KeyPairManager keyPairManager = new KeyPairManager();
        String privateKeyFileName = request.getId() + "_public.key";
        keyPairManager.create();
        keyPairManager.save(privateKeyFileName);

        // Doctor DB에 저장
        Subject subject = null;

        switch (request.getSubject()) {
            case 1:
                subject = Subject.IM;
                break;
            case 2:
                subject = Subject.EY;
                break;
            case 3:
                subject = Subject.ENT;
                break;
            case 4:
                subject = Subject.GS;
                break;
            case 5:
                subject = Subject.OS;
                break;
            case 6:
                subject = Subject.NS;
                break;
            case 7:
                subject = Subject.DER;
                break;
            case 8:
                subject = Subject.PED;
                break;
            case 9:
                subject = Subject.OBGY;
                break;
            case 10:
                subject = Subject.PSY;
                break;
        }

        Doctor doctor = new Doctor(request.getId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                keyPairManager.getPublicKey().getEncoded(),
                subject);

        doctorRepository.save(doctor);

        return doctor;
    }

    /* 아이디 중복 확인 */
    public boolean checkId(String id) {
        Optional<Member> member = memberRepository.findById(id);

        if (member.isPresent()) {
            return false;
        }

        return true;
    }

    /* 로그인 */
    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        Optional<Member> optionalMember = this.memberRepository.findById(username);

        if (optionalMember.isEmpty()) {
            System.out.println("user id" + username);
            throw new RuntimeException("해당 사용자가 없습니다.");
        }

        Member member = optionalMember.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new User(member.getId(), member.getPassword(), authorities);
    }
}
