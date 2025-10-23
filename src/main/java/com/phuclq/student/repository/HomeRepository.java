package com.phuclq.student.repository;

import com.phuclq.student.domain.Blog;
import com.phuclq.student.domain.CategoryBLog;
import com.phuclq.student.domain.Home;
import com.phuclq.student.dto.HomeDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {


    @Query(value = "select j.id         as id,j.CREATED_DATE as createdDate,j.created_by as createBy, address      as address,  content      as content, district_id  as districtId,   email        as email,is_deleted   as isDeleted,\n" +
            "       phone        as phone,\n" +
            "       province_id  as provinceId,\n" +
            "       price        as price,\n" +
            "       ward_id      as wardId,\n" +
            "       p._name      as provinceName,\n" +
            "       d._name      as districtName,\n" +
            "       w._name      as wardName,\n" +
            "       j.title      as title , j.NAME_USER as nameUser, j.closed as closed, j.acreage as acreage, j.shared as shared, j.AIR_CONDITION as airCondition , " +
            " j.fridge as fridge, j.WASHING_MACHINE as washingMachine ,j.name as name " +
            "\n" +
            "from home j\n" +
            "\n" +
            "         join province p on j.province_id = p.id\n" +
            "\n" +
            "         join district d on j.district_id = d.id\n" +
            "\n" +
            "         join ward w on j.ward_id = w.id\n" +
            "where j.ID_URL = :idUrl ", nativeQuery = true)
    HomeDetailDTO findAllById(@Param("idUrl") String idUrl);


    List<Home> findByIdUrlStartingWith(String id);


    List<Home> findAllByIdUrlIsNullOrIdUrl(String idUrl);
}
