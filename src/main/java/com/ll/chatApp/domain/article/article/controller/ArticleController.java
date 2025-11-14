package com.ll.chatApp.domain.article.article.controller;

import com.ll.chatApp.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
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
public class ArticleController {
    private static final List<String> SUPPORTED_KW_TYPES = List.of(
            "authorUsername",
            "title",
            "body",
            "tagContent",
            "commentAuthorUsername",
            "commentBody"
    );
    private static final List<String> DEFAULT_KW_TYPES = List.of("title", "body");

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
        String normalizedKw = kw == null ? "" : kw.trim();
        List<String> resolvedKwTypes = resolveKwTypes(kwTypes);
        Map<String, Boolean> kwTypeMap = buildKwTypeMap(resolvedKwTypes);

        var itemsPage = articleService.search(normalizedKw, resolvedKwTypes, pageable);
        model.addAttribute("kw", normalizedKw);
        model.addAttribute("kwTypeMap", kwTypeMap);
        model.addAttribute("itemsPage", itemsPage);
        return "article/list";
    }

    private List<String> resolveKwTypes(List<String> kwTypes) {
        if (kwTypes == null || kwTypes.isEmpty()) {
            return DEFAULT_KW_TYPES;
        }

        List<String> filtered = kwTypes.stream()
                .filter(SUPPORTED_KW_TYPES::contains)
                .distinct()
                .toList();

        return filtered.isEmpty() ? DEFAULT_KW_TYPES : filtered;
    }

    private Map<String, Boolean> buildKwTypeMap(List<String> resolvedKwTypes) {
        return SUPPORTED_KW_TYPES.stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        resolvedKwTypes::contains,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}
