package me.shinsunyoung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.ArticleResponse;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody @Validated AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle); //201: 요청 수행 성공 및 새로운 리소스 생성
    }

    @GetMapping(value= "/api/articles", produces = "application/json; charset=utf8")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream().map(ArticleResponse::new).toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping(value= "/api/articles/{id}", produces = "application/json; charset=utf8")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable(name = "id") Long id) { // @PathVariable은 url의 id에 해당하는 값이 id로 들어옴
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping(value= "/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable(name = "id") Long id) { // @PathVariable은 url의 id에 해당하는 값이 id로 들어옴
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value= "/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") Long id, @RequestBody UpdateArticleRequest request) { // @PathVariable은 url의 id에 해당하는 값이 id로 들어옴
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(updatedArticle);
    }
}
