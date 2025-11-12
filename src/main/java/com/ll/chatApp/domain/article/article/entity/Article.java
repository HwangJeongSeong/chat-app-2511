package com.ll.chatApp.domain.article.article.entity;

import com.ll.chatApp.domain.article.article.articleComment.entity.ArticleComment;
import com.ll.chatApp.domain.member.member.entity.Member;
import com.ll.chatApp.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Article extends BaseEntity {
    String title;
    String content;
    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "article", cascade = ALL)
    @Builder.Default
    private List<ArticleComment> comments = new ArrayList<>();

    public void addComment(Member memeberAuthor, String commentBody) {
        ArticleComment comment = ArticleComment.builder()
                .article(this)
                .author(memeberAuthor)
                .body(commentBody)
                .build();

        comments.add(comment);


    }
}