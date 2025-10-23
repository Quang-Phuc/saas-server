package com.phuclq.student.repository;

import com.phuclq.student.domain.FilePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilePriceRepository extends JpaRepository<FilePrice, Integer> {

    FilePrice findByFileId(Integer fileId);

}
