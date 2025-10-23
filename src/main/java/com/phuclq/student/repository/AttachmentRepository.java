package com.phuclq.student.repository;

import com.phuclq.student.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {


    List<Attachment> findAllByRequestIdInAndFileTypeIn(List<Integer> requestId, List<String> typeFile);

    List<Attachment> findAllByRequestIdInAndFileType(List<Integer> requestId, String typeFile);

    List<Attachment> findAllByRequestIdAndFileTypeIn(Integer requestId, List<String> typeFile);

    List<Attachment> findAllByRequestIdAndFileType(Integer requestId, String fileType);

    Optional<Attachment> findAllByIdAndFileType(Long id, String fileType);

    @Query("SELECT a FROM ATTACHMENT a WHERE a.codeFile IN " +
            "(SELECT b.codeFile FROM ATTACHMENT b WHERE b.fileType = 'FILE_UPLOAD' and b.checkDuplicate is null  " +
            "GROUP BY b.codeFile HAVING COUNT(b) > 1)")
    List<Attachment> findDuplicateFiles();

}
