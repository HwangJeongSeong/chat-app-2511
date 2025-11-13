package com.ll.chatApp.domain.article.articleTag.service;

import com.ll.chatApp.domain.article.articleTag.entity.ArticleTag;
import com.ll.chatApp.domain.article.articleTag.repository.ArticleTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleTagService {
    private final ArticleTagRepository articleTagRepository;

    public List<ArticleTag> findByAuthorId(Long authorId) {
        return articleTagRepository.findByArticleAuthorId(authorId);
    }

    public List<ArticleTag> findByAuthorUsername(String authorUsername) {
        return articleTagRepository.findByArticleAuthorUsername(authorUsername);
    }
}
