package com.phuclq.student.service;

import com.phuclq.student.domain.Blog;
import com.phuclq.student.domain.CategoryBLog;
import com.phuclq.student.domain.UserHistoryBlog;
import com.phuclq.student.dto.HistoryFileResult;
import com.phuclq.student.dto.TotalMyDTO;
import com.phuclq.student.dto.blog.BlogDetailDto;
import com.phuclq.student.dto.blog.BlogRequest;
import com.phuclq.student.dto.blog.BlogResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface BlogService {


    Long create(BlogRequest dto) throws IOException;

    Long createCategoryBlog(CategoryBLog dto);

    UserHistoryBlog activityHome(Long id, Integer activity);

    BlogResultDto searchBlog(Pageable pageable, BlogRequest search);

    List<CategoryBLog> searchCategoryBlog();

    Page<CategoryBLog> findAll(Pageable pageable, String search);

    BlogDetailDto findAllBlogById(String idUrlCategory, String idUrl);

    CategoryBLog findAllCategoryBlogById(Long id);

    void deleteBlogById(Long id);

    void deleteCategoryBlogById(Long id);

    void approve(Long id);


    BlogResultDto myHome(BlogRequest request, Pageable pageable);

    TotalMyDTO total();

    void deleteActivityHome(Long id, Integer card);

    Page<HistoryFileResult> deleteHistory(BlogRequest request);
}
