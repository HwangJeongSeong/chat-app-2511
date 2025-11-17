package com.ll.chatApp.domain.article.article.repository;

import com.ll.chatApp.domain.article.article.entity.Article;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ll.chatApp.domain.article.article.entity.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Article> search(List<String> kwTypes, String kw, Pageable pageable) {
        BooleanBuilder builder = buildPredicate(kwTypes, kw);

        JPAQuery<Article> articlesQuery = jpaQueryFactory
                .selectDistinct(article)
                .from(article)
                .where(builder);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(article.getType(), article.getMetadata());
            articlesQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        articlesQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(article.count())
                .from(article)
                .where(builder);

        return PageableExecutionUtils.getPage(articlesQuery.fetch(), pageable, totalQuery::fetchOne);
    }

    private BooleanBuilder buildPredicate(List<String> kwTypes, String kw) {
        BooleanBuilder builder = new BooleanBuilder();

        if (kw == null || kw.isBlank() || kwTypes == null) {
            return builder;
        }

        if (kwTypes.contains("title")) {
            builder.or(article.title.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("content")) {
            builder.or(article.content.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("authorUsername")) {
            builder.or(article.author.username.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("tagContent")) {
            builder.or(article.tags.any().content.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("commentAuthorUsername")) {
            builder.or(article.comments.any().author.username.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("commentBody")) {
            builder.or(article.comments.any().body.containsIgnoreCase(kw));
        }

        return builder;
    }
}
