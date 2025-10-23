package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.UserInformation;
import com.phuclq.student.dto.SignatureDTO;
import com.phuclq.student.dto.UserInformationDTO;
import com.phuclq.student.dto.UserInformationDetailDTO;
import com.phuclq.student.service.UserInformationService;
import com.phuclq.student.utils.SignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.validation.Valid;

import static com.phuclq.student.utils.ReplaceInJson.replaceInJson;

@RestController
@RequestMapping("/api")
public class UserInformationController {
    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("/user-infor/create")
    public ResponseEntity<?> createUserInfor(@RequestBody @Valid UserInformationDTO userInformationDTO) throws Exception {
        UserInformation userInformation = userInformationService.save(userInformationDTO);

        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(userInformation).getResponse();

    }

    @PutMapping("/user-infor/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UserInformationDTO userInformationDTO) {
        UserInformation userInformation = userInformationService.update(userInformationDTO);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(userInformation).getResponse();
    }

    @DeleteMapping("/user-infor/delete/{Id}")
    public ResponseEntity<?> deleteUserInfo(@PathVariable Integer Id) {
        userInformationService.delete(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }

    @GetMapping("/user-infor-id")
    public ResponseEntity<?> getUserInforById() {
        UserInformationDetailDTO userInformationDTO = userInformationService.listById();
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(userInformationDTO).getResponse();
    }
    @PostMapping("/signature")
    public SignatureDTO signature(  @RequestParam("privateKey") String privateKey,@RequestBody Object files) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> filesMap = objectMapper.convertValue(files, Map.class);
        String jsonString = replaceInJson(objectMapper.writeValueAsString(filesMap));

        SignatureDTO signatureDTO = new SignatureDTO();
        signatureDTO.setSignature(SignatureUtil.signature2(jsonString,privateKey));
        return signatureDTO;
    }

}
