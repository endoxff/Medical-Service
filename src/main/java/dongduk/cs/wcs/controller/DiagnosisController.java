package dongduk.cs.wcs.controller;

import dongduk.cs.wcs.domain.Diagnosis;
import dongduk.cs.wcs.domain.Doctor;
import dongduk.cs.wcs.dto.DiagnosisReqDto;
import dongduk.cs.wcs.dto.DiagnosisResDto;
import dongduk.cs.wcs.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    // 진료 요청 Form
    @GetMapping("/request")
    public String diagnosisRequestForm(Model model) {
        List<Doctor> doctors = diagnosisService.diagnosisRequestForm();
        model.addAttribute("doctors", doctors);
        model.addAttribute("requestDiagnosisForm", new DiagnosisReqDto.DiagnosisRequestReqDto());

        return "diagnosis_request_form";
    }

    // 진료 요청
    @PostMapping("/request")
    public String diagnosisReqest(@Valid @ModelAttribute("requestDiagnosisForm")DiagnosisReqDto.DiagnosisRequestReqDto request,
                                  BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "diagnosis_request_form";
        }

        diagnosisService.diagnosisRequest(principal.getName(), request);

        return "redirect:/";
    }

    // 진료 요청 확인
    @GetMapping("/request/list")
    public String diagnosisRequestCheck(Model model, Principal principal) {
        List<Diagnosis> diagnosisList = diagnosisService.diagnosisRequestCheck(principal.getName());
        model.addAttribute("diagnosisList", diagnosisList);

        return "diagnosis_request_check";
    }

    // 진료 요청 상세 확인
    @GetMapping("/request/detail/{id}")
    public String diagnosisRequestDetailCheck(@PathVariable Long id, Model model) {
        DiagnosisResDto.DiagnosisRequestResDto diagnosis = diagnosisService.diagnosisRequestDetailCheck(id);
        model.addAttribute("diagnosis", diagnosis);

        return "diagnosis_request_check_detail";
    }

    // 진료 응답 Form
    @GetMapping("/response/{id}")
    public String diagnosisResponseForm(@PathVariable Long id, Model model) {
        Map<String, String> receiver = diagnosisService.diagnosisResponseForm(id);
        model.addAttribute("receiver", receiver);
        model.addAttribute("responseDiagnosisForm", new DiagnosisReqDto.DiagnosisResponseReqDto());

        return "diagnosis_response_form";
    }

    // 진료 응답
    @PostMapping("/response")
    public String diagnosisResponse(@Valid @ModelAttribute("responseDiagnosisForm")DiagnosisReqDto.DiagnosisResponseReqDto request,
                                    BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "diagnosis_response_form";
        }

        diagnosisService.diagnosisResponse(principal.getName(), request);

        return "redirect:/";
    }

    // 진료 응답 확인
    @GetMapping("/response/list")
    public String diagnosisResponseCheck(Model model, Principal principal) {
        List<Diagnosis> diagnosisList = diagnosisService.diagnosisRequestCheck(principal.getName());
        model.addAttribute("diagnosisList", diagnosisList);

        return "diagnosis_response_check";
    }

    // 진료 응답 상세 획인
    @GetMapping("/response/detail/{id}")
    public String diagnosisResponseDetailCheck(@PathVariable Long id, Model model) {
        DiagnosisResDto.DiagnosisRequestResDto diagnosis = diagnosisService.diagnosisRequestDetailCheck(id);
        model.addAttribute("diagnosis", diagnosis);

        return "diagnosis_response_check_detail";
    }
}
