package com.ll.chatApp.domain.article.article.controller;

import com.ll.chatApp.domain.article.article.entity.Article;
import com.ll.chatApp.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    @GetMapping("/list")
    public String list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw,
            @RequestParam(value = "kwType", required = false) List<String> kwTypes,
            Model model
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        List<String> resolvedKwTypes = (kwTypes == null || kwTypes.isEmpty())
                ? List.of("title", "body")
                : kwTypes;

        Map<String, Boolean> kwTypeMap = resolvedKwTypes.stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        Page<Article> itemsPage = articleService.search(pageable);
        model.addAttribute("kw", kw);
        model.addAttribute("kwTypeMap", kwTypeMap);
        model.addAttribute("itemsPage", itemsPage);
        return "article/list";
    }
}
