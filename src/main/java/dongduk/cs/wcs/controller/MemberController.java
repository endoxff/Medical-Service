package dongduk.cs.wcs.controller;

import dongduk.cs.wcs.dto.MemberReqDto;
import dongduk.cs.wcs.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /* 환자 회원 가입 Form */
    @GetMapping("/signup/patient")
    public String patientSignupForm(Model model) {
        model.addAttribute("signupForm", new MemberReqDto.PatientSignupReqDto());
        return "patient_signup_form";
    }

    /* 환자 회원 가입 */
    @PostMapping("/signup/patient")
    public String patientSignup(@Valid @ModelAttribute("signupForm") MemberReqDto.PatientSignupReqDto request,
                                BindingResult bindingResult) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "patient_signup_form";
        }

        memberService.patientSignup(request);

        return "redirect:/";
    }

    /* 의사 회원 가입 Form */
    @GetMapping("/signup/doctor")
    public String doctorSignupForm(Model model) {
        model.addAttribute("signupForm", new MemberReqDto.DoctorSignupReqDto());
        return "doctor_signup_form";
    }

    /* 의사 회원 가입 */
    @PostMapping("/signup/doctor")
    public String doctorSignup(@Valid @ModelAttribute("signupForm") MemberReqDto.DoctorSignupReqDto request,
                               BindingResult bindingResult) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "doctor_signup_form";
        }

        memberService.doctorSignup(request);

        return "redirect:/";
    }

    /* 로그인 Form */
    @GetMapping("/login")
    public String loginForm() {
        return "login_form";
    }
}
