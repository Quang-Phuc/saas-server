package com.phuclq.student.repository;

import com.phuclq.student.domain.File;
import com.phuclq.student.dto.FileResultInterface;
import com.phuclq.student.dto.HistoryFileResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    String sql = "select f.id as id, f.title as title, date_format(f.created_date, '%d/%m/%Y') as createDate, f.image as image, " +
            "f.view as view, f.dowloading as downloading, f.dowloading * fp.price * 0.7 as money " +
            "from file f inner join user_history_file uhf on " +
            "f.id = uhf.file_id inner join user_history uh on uh.id = uhf.user_hisoty_id " +
            "inner join activity a on a.id = uh.activity_id left join file_price fp " +
            "on fp.file_id = f.id where (f.is_deleted is null or f.is_deleted = false) ";

    String sqlnotactivity = "select f.id as id, f.title as title, date_format(f.created_date, '%d/%m/%Y') as createDate, f.image as image, " +
            "f.view as view, f.dowloading as downloading, f.dowloading * fp.price * 0.7 as money " +
            "from file f " +
            " left join file_price fp " +
            "on fp.file_id = f.id where (f.is_deleted is null or f.is_deleted = false) ";

    String sqlfileResult = "select f.id as id, f.title as title, f.view as view, f.dowloading as download, fp.price as price"
            + ", f.image as image, date_format(f.created_date, '%d/%m/%Y') as createDate from file "
            + "f inner join file_price fp on f.id = fp.file_id where f.approver_id is not null ";
    String sqlfileTop8 = "select f.id as id, f.title as title, f.view as view, f.dowloading as download, fp.price as price"
            + ", f.image as image, date_format(f.created_date, '%d/%m/%Y') as createDate from file "
            + "f inner join file_price fp on f.id = fp.file_id where f.approver_id is not null and f.is_vip not null order BY case when f.start_money_top is null then 1 else 0 end,f.start_money_top,f.id desc LIMIT 9 ";

    @Query(value = "select * from file f where f.category_id = :id", nativeQuery = true)
    Page<File> findFilesByCategory(@Param("id") Integer id, Pageable pageable);

    Optional<File> findById(Integer id);

    Optional<File> findByIdUrl(String id);

    List<File> findByIdUrlStartingWith(String id);

    List<File> findByMoneyTopIsNotNull();

    @Query(value = "select f.* " +
            "from file f inner join category c on f.category_id = c.id inner join specialization spec on f.specialization_id = spec.id " +
            "inner join school s on f.school_id = s.id inner join file_price fpr on f.id = fpr.file_id " +
            "where (:category is null or c.id = :category) " +
            "and (:specialization is null or spec.id = :specialization) " +
            "and (:school is null or s.id = :school) " +
            "and (:title is null or f.title like CONCAT('%',:title,'%')) " +
            "and (:isVip is null or f.is_vip = :isVip) " +
            "and (:price is null or fpr.price >= :price) " +
            "and (:price is null or fpr.price <= :price)"
            , nativeQuery = true)
    Page<File> searchFiles(@Param("category") Integer category, @Param("specialization") Integer specialization, @Param("school") Integer school
            , @Param("title") String title, @Param("isVip") Boolean isVip, @Param("price") Float price, Pageable pageable);

    File findByFileHashcode(String fileHascode);

    @Query(value = "select count(category_id) as countCategory from file where category_id =:idCategory and  approver_id IS NOT NULL and delete_id IS  NULL"
            , nativeQuery = true)
    Integer countCategoriesHome(@Param("idCategory") Long idCategory);

    @Query(value = "select * from file  where category_id =:categoryId LIMIT 3"
            , nativeQuery = true)
    List<File> findAllByCategoryId(Long categoryId);

    @Query(value = "select * from file where created_date between ?1 and ?2 and "
            + "title = ?3 and author_id = ?4", nativeQuery = true)
    Page<File> searchPostUser(Timestamp createDateFrom, Timestamp createDateTo, String title, Integer authorId, Pageable pageable);

    @Query(value = "select * from file where created_date between ?1 and ?2 and "
            + "title like concat('%',?3,'%') ", nativeQuery = true)
    Page<File> searchPostAdmin(Timestamp createDateFrom, Timestamp createDateTo, String title, Pageable pageable);

    @Query(value = "select * from file where "
            + "title = ?1 and author_id = ?2 ", nativeQuery = true)
    Page<File> searchPostUserNotDate(String title, Integer authorId, Pageable pageable);

    @Query(value = "select * from file where "
            + "title like concat('%',?1,'%') ", nativeQuery = true)
    Page<File> searchPostAdminNotDate(String title, Pageable pageable);

    //
    @Query(value = sql + "and f.created_date between ?1 and ?2 and f.title like concat('%',?3,'%')"
            + " and uh.user_id = ?4", nativeQuery = true)
    Page<HistoryFileResult> getFileByUser(Timestamp dateFrom, Timestamp dateTo, String title,
                                          Integer userId, Pageable pageable);

    @Query(value = sql + "and f.approved_date is not null and f.created_date between ?1 "
            + "and ?2 and f.title like concat('%',?3,'%') and uh.user_id = ?4", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserApprovedFile(Timestamp dateFrom, Timestamp dateTo, String title,
                                                      Integer userId, Pageable pageable);

    @Query(value = sqlnotactivity + "and f.approved_date is null and f.created_date between ?1 "
            + "and ?2 and f.title like concat('%',?3,'%') and f.author_id = ?4", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserApprovingFile(Timestamp dateFrom, Timestamp dateTo, String title,
                                                       Integer userId, Pageable pageable);

    //
    @Query(value = sql + "and f.title like concat('%',?1,'%') and uh.user_id = ?2", nativeQuery = true)
    Page<HistoryFileResult> getFileByUser(String title, Integer userId, Pageable pageable);

    @Query(value = sql + "and f.approved_date is not null and f.title like concat('%',?1,'%')"
            + " and uh.user_id = ?2", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserApprovedFile(String title, Integer userId, Pageable pageable);

    @Query(value = sqlnotactivity + "and f.approved_date is null and f.title like concat('%',?1,'%')"
            + " and f.author_id = ?2", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserApprovingFile(String title, Integer userId, Pageable pageable);

    //
    @Query(value = sql + "and a.activity = 1 and f.title like concat('%',?1,'%')"
            + " and uh.user_id = ?2", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserDownloaded(String title, Integer userId, Pageable pageable);

    @Query(value = sql + "and a.activity = 1 and f.created_date between ?1 "
            + "and ?2 and f.title like concat('%',?3,'%') and uh.user_id = ?4", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserDownloaded(Timestamp dateFrom, Timestamp dateTo, String title,
                                                    Integer userId, Pageable pageable);

    //
    @Query(value = sql + "and a.activity = 4 and uh.user_id = ?1", nativeQuery = true)
    Page<HistoryFileResult> getFileByUserFavorite(Integer userId, Pageable pageable);

    @Query(value = sql + "and a.activity = 4 and uhf.activity_date >= ?1 and uhf.activity_date <= ?2 and uh.user_id = ?3", nativeQuery = true)
    Page<HistoryFileResult> getFileUserFavoriteByDate(Timestamp dateFrom, Timestamp dateTo, Integer userId, Pageable pageable);

    @Query(value = "select f.id as id, f.title as title, f.view as view, f.dowloading as download, fp.price as price"
            + ", f.image as image, date_format(f.created_date, '%d/%m/%Y') as createDate from file "
            + "f inner join file_price fp on f.id = fp.file_id inner join category c on f.category_id = c.id "
            + " where f.approver_id is not null and c.id = ?1", nativeQuery = true)
    Page<FileResultInterface> findByCategoryId(Long categoryId, Pageable pageable);

    Page<File> findByApproverId(Integer approverId, Pageable pageable);

    @Query(value = sqlfileTop8, nativeQuery = true)
    List<File> findTop8FileOrderByIdDesc();

}
