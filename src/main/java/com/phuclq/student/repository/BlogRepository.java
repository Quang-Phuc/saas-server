package com.phuclq.student.repository;

import com.phuclq.student.domain.Blog;
import com.phuclq.student.domain.File;
import com.phuclq.student.dto.blog.BlogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findAllByIdIn(List<Long> ids);

    Optional<Blog> findAllByIdUrl(String idUrl);

    List<Blog> findByIdUrlStartingWith(String id);

    List<Blog> findAllByIdUrlIsNullOrIdUrl(String idUrl);

    List<Blog> findAllByCategoryBlogId(Long categoryId);


    @Query("SELECT new com.phuclq.student.dto.blog.BlogDto(b.id, b.categoryBlogId, b.categoryBlogName, b.idUrl, b.title) FROM BLOG b WHERE b.categoryBlogId = :categoryBlogId")
    List<BlogDto> findByCategory(@Param("categoryBlogId") Long id);

}
