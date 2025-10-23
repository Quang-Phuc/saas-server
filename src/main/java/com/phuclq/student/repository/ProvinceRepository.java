package com.phuclq.student.repository;

import com.phuclq.student.domain.Province;
import com.phuclq.student.dto.AddressResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    @Query(" select pr.id    as provinceId,\n" +
            "       pr.name as provinceName,\n" +
            "       di.id     as  districtId,\n" +
            "       di.name as districtName,\n" +
            "       w.id     as wardId,\n" +
            "       w.name  as wardName\n" +
            "from Province pr\n" +
            "         join District di\n" +
            "              on pr.id = di.provinceId\n" +
            "         join Ward w on di.id = w.districtId and w.provinceId = pr.id\n   ")
    List<AddressResult> findAddress();

    @Query(" select pr.id    as provinceId,\n" +
            "       pr.name as provinceName,\n" +
            "       di.id     as  districtId,\n" +
            "       di.name as districtName,\n" +
            "       w.id     as wardId,\n" +
            "       w.name  as wardName\n" +
            "from Province pr\n" +
            "         join District di\n" +
            "              on pr.id = di.provinceId\n" +
            "         join Ward w on di.id = w.districtId and w.provinceId = pr.id\n " +
            "where upper(pr.name) like  %:search%  or  upper(di.name) like %:search% or  upper(w.name) like %:search%  ")
    List<AddressResult> findAddressBySearch(@Param("search") String search);

}
