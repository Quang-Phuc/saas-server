package com.phuclq.student.service;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDetailResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface PledgeContractService {

    PledgeContract createPledge(
            String payloadJson,
            MultipartFile portraitFile,
            List<MultipartFile> attachmentFiles
    );

    public PledgeContractDetailResponse getPledgeDetail(Long id) ;

    // (Thêm các hàm khác sau: update, getById, ...)
}