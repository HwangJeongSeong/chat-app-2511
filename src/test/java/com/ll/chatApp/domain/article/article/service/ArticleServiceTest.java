package com.ll.chatApp.domain.article.article.service;

import com.ll.chatApp.domain.article.article.articleComment.entity.ArticleComment;
import com.ll.chatApp.domain.article.article.entity.Article;
import com.ll.chatApp.domain.member.member.entity.Member;
import com.ll.chatApp.domain.member.member.service.MemberService;
import com.ll.chatApp.global.rsData.RsData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MemberService memberService;

    @DisplayName("글 쓰기")
    @Test
    void t1() {
        Member member = createMember("t1");
        RsData<Article> writeRs = articleService.write(member.getId(), "제목", "내용");
        Article article = writeRs.getData();

        assertThat(article.getId()).isGreaterThan(0L);
    }

    @DisplayName("저장된 글을 id로 가져온다")
    @Test
    void t2() {
        Article saved = createArticle("t2", "제목1", "내용1");
        Article article = articleService.findById(saved.getId()).orElseThrow();

        assertThat(article.getTitle()).isEqualTo("제목1");
    }

    @DisplayName("글 작성자의 username을 확인한다")
    @Test
    void t3() {
        Member member = createMember("t3");
        Article saved = articleService.write(member.getId(), "제목", "내용").getData();
        Article article = articleService.findById(saved.getId()).orElseThrow();

        assertThat(article.getAuthor().getUsername()).isEqualTo(member.getUsername());
    }

    @DisplayName("글을 수정한다")
    @Test
    void t4() {
        Article article = createArticle("t4", "원래 제목", "원래 내용");

        articleService.modify(article, "수정된 제목", "수정된 내용");

        Article modified = articleService.findById(article.getId()).orElseThrow();
        assertThat(modified.getTitle()).isEqualTo("수정된 제목");
    }

    private Member createMember(String suffix) {
        return memberService.join("user_%s".formatted(suffix), "1234").getData();
    }

    private Article createArticle(String suffix, String title, String content) {
        Member member = createMember("article_" + suffix);
        return articleService.write(member.getId(), title, content).getData();
    }

    @DisplayName("1번 글의 댓글들을 수정한다.")
    @Test
    void t6() {
        Article article = articleService.findById(2L).get();

        article.getComments().forEach(comment -> {
            articleService.modifyComment(comment, comment.getBody() + "!!");
        });
    }

    @DisplayName("1번 글의 댓글 중 마지막 것을 삭제한다.")
    @Test
    void t7() {
        Article article = articleService.findById(2L).get();

        ArticleComment lastComment = article.getComments().getLast();

        article.removeComment(lastComment);
    }
}
