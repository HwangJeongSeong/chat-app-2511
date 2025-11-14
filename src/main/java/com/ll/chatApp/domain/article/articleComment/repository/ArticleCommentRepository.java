package com.ll.chatApp.domain.article.articleComment.repository;

import com.ll.chatApp.domain.article.articleComment.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findByAuthorId(Long authorId);
}
