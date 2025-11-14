package com.ll.chatApp.domain.article.article.service;

import com.ll.chatApp.domain.article.article.entity.Article;
import com.ll.chatApp.domain.article.article.repository.ArticleRepository;
import com.ll.chatApp.domain.article.articleComment.entity.ArticleComment;
import com.ll.chatApp.domain.article.articleTag.entity.ArticleTag;
import com.ll.chatApp.domain.member.member.entity.Member;
import com.ll.chatApp.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional
    public RsData<Article> write(Long memberId, String title, String content) {
        Article article = Article.builder()
                .author(Member.builder().id(memberId).build())
                .title(title)
                .content(content)
                .build();

        articleRepository.save(article);

        return RsData.of("200", "글 작성 성공", article);
    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);

//        articleRepository.save(article);
    }

    @Transactional
    public void modifyComment(ArticleComment comment, String commnetBody) {
        comment.setBody(commnetBody);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Page<Article> search(String kw, List<String> kwTypes, Pageable pageable) {
        if (kw == null || kw.isBlank()) {
            return articleRepository.findAll(pageable);
        }

        if (kwTypes == null || kwTypes.isEmpty()) {
            return articleRepository.findAll(pageable);
        }

        Specification<Article> specification = buildSpecification(kw, kwTypes);
        return articleRepository.findAll(specification, pageable);
    }

    private Specification<Article> buildSpecification(String kw, List<String> kwTypes) {
        return (root, query, cb) -> {
            query.distinct(true);

            String pattern = "%" + kw.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            Join<Article, Member> authorJoin = null;
            Join<Article, ArticleTag> tagJoin = null;
            Join<Article, ArticleComment> commentJoin = null;
            Join<ArticleComment, Member> commentAuthorJoin = null;

            for (String kwType : kwTypes) {
                switch (kwType) {
                    case "authorUsername" -> {
                        if (authorJoin == null) {
                            authorJoin = root.join("author", JoinType.LEFT);
                        }
                        predicates.add(cb.like(cb.lower(authorJoin.get("username")), pattern));
                    }
                    case "title" -> predicates.add(cb.like(cb.lower(root.get("title")), pattern));
                    case "body" -> predicates.add(cb.like(cb.lower(root.get("content")), pattern));
                    case "tagContent" -> {
                        if (tagJoin == null) {
                            tagJoin = root.join("tags", JoinType.LEFT);
                        }
                        predicates.add(cb.like(cb.lower(tagJoin.get("content")), pattern));
                    }
                    case "commentAuthorUsername" -> {
                        if (commentJoin == null) {
                            commentJoin = root.join("comments", JoinType.LEFT);
                        }
                        if (commentAuthorJoin == null) {
                            commentAuthorJoin = commentJoin.join("author", JoinType.LEFT);
                        }
                        predicates.add(cb.like(cb.lower(commentAuthorJoin.get("username")), pattern));
                    }
                    case "commentBody" -> {
                        if (commentJoin == null) {
                            commentJoin = root.join("comments", JoinType.LEFT);
                        }
                        predicates.add(cb.like(cb.lower(commentJoin.get("body")), pattern));
                    }
                    default -> {
                    }
                }
            }

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
