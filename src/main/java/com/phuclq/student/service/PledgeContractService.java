package com.phuclq.student.service;

import com.phuclq.student.domain.PledgeContract;
import com.phuclq.student.dto.PledgeContractDetailResponse;
import com.phuclq.student.dto.PledgeContractDto;
import com.phuclq.student.dto.PledgeContractListResponse;
import com.phuclq.student.dto.PledgeSearchRequest;
import com.phuclq.student.types.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PledgeContractService {

    PledgeContract createPledge(
            String payloadJson,
            MultipartFile portraitFile,
            List<MultipartFile> attachmentFiles
    );
    PledgeContract updatePledge(Long id, String payloadJson, MultipartFile portraitFile, List<MultipartFile> attachmentFiles);

    public PledgeContractDetailResponse getPledgeDetail(Long id) ;

    Page<PledgeContractListResponse> searchPledges(PledgeSearchRequest request);

    PledgeContractDto getContractDetail(Long id);
}