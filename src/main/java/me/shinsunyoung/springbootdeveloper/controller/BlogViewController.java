package me.shinsunyoung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.ArticleListViewResponse;
import me.shinsunyoung.springbootdeveloper.dto.ArticleViewResponse;
import me.shinsunyoung.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new).toList();

        model.addAttribute("articles", articles);

        return "articleList"; //articleList.html이라는 뷰 조회
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable(name = "id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article"; //article.html이라는 뷰 조회
    }
    
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false, name = "id") Long id, Model model) {
        if (id == null) { // 블로그 글 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 블로그 글 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        
        return "newArticle"; //newArticle.html 뷰로 이동
    }
}
