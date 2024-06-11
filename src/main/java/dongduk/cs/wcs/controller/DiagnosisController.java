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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Controller
@RequestMapping("/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    /* 진료 요청 Form */
    @GetMapping("/request")
    public String diagnosisRequestForm(Model model) {
        List<Doctor> doctors = diagnosisService.findAllDoctors();
        model.addAttribute("doctors", doctors);
        model.addAttribute("requestDiagnosisForm", new DiagnosisReqDto.DiagnosisRequestReqDto());

        return "diagnosis_request_form";
    }

    /* 진료 요청 */
    @PostMapping("/request")
    public String diagnosisReqest(@Valid @ModelAttribute("requestDiagnosisForm")DiagnosisReqDto.DiagnosisRequestReqDto request, BindingResult bindingResult, Principal principal)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            System.out.println(request.getReceiver());
            return "diagnosis_request_form";
        }

        diagnosisService.diagnosisRequest(principal.getName(), request);

        return "redirect:/";
    }

    /* 진료 요청 확인 */
    @GetMapping("/request/list")
    public String diagnosisRequestCheck(Model model, Principal principal) {
        List<Diagnosis> diagnosisList = diagnosisService.findAllDiagnosis(principal.getName());
        model.addAttribute("diagnosisList", diagnosisList);

        return "diagnosis_request_check";
    }

    /* 진료 요청 세부 확인 */
    @GetMapping("/request/detail/{id}")
    public String diagnosisRequestDetailCheckForm(@PathVariable Long id, Model model)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        DiagnosisResDto.DiagnosisRequestResDto diagnosis = diagnosisService.diagnosisRequestCheck(id);
        model.addAttribute("diagnosis", diagnosis);

        return "diagnosis_request_check_detail";
    }
}
