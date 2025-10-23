package com.phuclq.student.repository;

import com.phuclq.student.domain.Banner;
import com.phuclq.student.dto.school.SchoolResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query(" select rh.createdBy as createdBy , rh.createdDate as createdDate , rh.id as id ,rh.title as title from BANNER rh  ")
    Page<SchoolResult> findAllBySchoolNameContainingIgnoreCaseQuery(Pageable pageable);

    @Query(" select rh.createdBy as createdBy , rh.createdDate as createdDate , rh.id as id ,rh.title as title from BANNER rh where  upper(rh.title)  =:search ")
    Page<SchoolResult> findAllBySchoolNameContainingIgnoreCaseQuerySearch(@Param("search") String search, Pageable pageable);

    Page<Banner> findAllByTitleContainingIgnoreCase(String search, Pageable pageable);

    Page<Banner> findAllByTypeContainingIgnoreCase(String search, Pageable pageable);


    Optional<Banner> findAllByTitle(String type);

    Banner findAllByType(String type);


}
