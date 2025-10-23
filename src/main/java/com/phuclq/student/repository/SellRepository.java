package com.phuclq.student.repository;

import com.phuclq.student.domain.Sell;
import com.phuclq.student.dto.sell.SellDTO;
import com.phuclq.student.dto.sell.SellDTOIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    List<Sell> findAllByIdIn(List<Long> ids);

    Optional<Sell> findByIdUrl(String idUrl);

    List<Sell> findByIdUrlStartingWith(String id);


    @Query("SELECT s.createdDate as createdDate,u.userName as userName,s.price as price,s.quantity as quantity, s.id AS id, s.sellCategoryId AS sellCategoryId, s.wardId AS wardId, s.districtId AS districtId, s.provinceId AS provinceId, s.content AS content, " +
            "s.title AS title, s.approverId AS approverId, s.approvedDate AS approvedDate, s.isDeleted AS isDeleted, s.moneyTop AS moneyTop, s.startMoneyTop AS startMoneyTop, " +
            "s.endMoneyTop AS endMoneyTop, s.totalCard AS totalCard, s.deleteId AS deleteId, s.deleteDate AS deleteDate, " +
            "sc.id AS categoryId, sc.name AS categoryName, " +
            " d.name AS districtName, d.prefix AS districtPrefix, d.provinceId AS districtProvinceId, " +
            "wa.name AS wardName, wa.prefix AS wardPrefix, wa.provinceId AS wardProvinceId, wa.districtId AS wardDistrictId, " +
            " pr.name AS provinceName, pr.code AS provinceCode, s.phone as phone " +
            "FROM SELL s " +
            "JOIN SELL_CATEGORY sc  on s.sellCategoryId = sc.id " +
            "JOIN User u  on u.id = s.createdBy " +
            "left join District d on s.districtId = d.id " +
            "left join Ward wa on wa.id = s.wardId left join Province pr on pr.id = s.provinceId " +
            "WHERE s.idUrl = :IdUrl")
    SellDTOIn findSellWithDetailsByIdUrl(@Param("IdUrl") String IdUrl);

}
