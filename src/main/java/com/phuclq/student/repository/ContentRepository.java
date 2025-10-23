package com.phuclq.student.repository;

import com.phuclq.student.domain.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(String search,String search2, Pageable pageable);


    Content findAllByType(String type);

}
