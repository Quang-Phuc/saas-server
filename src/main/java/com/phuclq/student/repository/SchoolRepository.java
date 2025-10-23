package com.phuclq.student.repository;

import com.phuclq.student.domain.School;
import com.phuclq.student.dto.school.SchoolResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    School findSchoolById(Long id);

    Optional<School> findByIdUrl(String idUrl);

    @Query(" select rh.schoolType as schoolType, rh.schoolTypeEducation as schoolTypeEducation, rh.createdBy as createdBy , rh.createdDate as createdDate , rh.id as id ,rh.schoolName as schoolName ,rh.content as content, d.name as districtName,rh.type as type ,rh.wardId as  wardId,rh.districtId as districtId, rh.provinceId as provinceId , rh.address as address,rh.summary as summary,rh.totalStudent as totalStudent,wa.name as wardName,pr.name as provinceName,rh.totalComment as totalComment ,rh.idUrl as  idUrl    from School rh left join District d on rh.districtId = d.id " + "left join Ward wa on wa.id = rh.wardId left join Province pr on pr.id = rh.provinceId where  (:schoolName is null or upper(rh.schoolName)  =:schoolName) " + "and (:wardId is null or rh.wardId  =:wardId) " + "and (:districtId is null or rh.districtId  =:districtId) " + "and (:provinceId is null or rh.provinceId  =:provinceId) " + "and (:schoolType is null or rh.schoolType  =:schoolType) " + "and (:schoolTypeEducation is null or rh.schoolTypeEducation  =:schoolTypeEducation)  ")
    Page<SchoolResult> findAllBySchoolNameContainingIgnoreCaseQuerySearch(@Param("schoolName") String schoolName, @Param("wardId") Integer wardId, @Param("districtId") Integer districtId, @Param("provinceId") Integer provinceId, @Param("schoolType") String schoolType, @Param("schoolTypeEducation") String schoolTypeEducation, Pageable pageable);

    School findAllById(Long id);

    School findAllByIdUrl(String idUrl);

    List<School> findByIdUrlStartingWith(String id);

}
