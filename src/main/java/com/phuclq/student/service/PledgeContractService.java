package com.phuclq.student.service;

import com.phuclq.student.domain.PledgeContract;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface PledgeContractService {

    PledgeContract createPledge(
            String payloadJson,
            MultipartFile portraitFile,
            List<MultipartFile> attachmentFiles
    );

    // (Thêm các hàm khác sau: update, getById, ...)
}