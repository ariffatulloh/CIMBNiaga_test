package com.example.cimbniaga_test.controller;

import com.example.cimbniaga_test.model.BlogPost;
import com.example.cimbniaga_test.model.PagedResponse;
import com.example.cimbniaga_test.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        BlogPost createdBlogPost = blogPostService.createBlogPost(blogPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlogPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable Integer id) {
        Optional<BlogPost> optionalBlogPost = blogPostService.getBlogPostById(id);
        if (optionalBlogPost.isPresent()) {
            return ResponseEntity.ok(optionalBlogPost.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<PagedResponse<BlogPost>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<BlogPost> blogPosts = blogPostService.getAllBlogPosts(page, size, sort, direction);

        PagedResponse<BlogPost> response = new PagedResponse<>(
                blogPosts.getContent(),
                blogPosts.getNumber(),
                blogPosts.getSize(),
                blogPosts.getTotalElements(),
                blogPosts.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(
            @PathVariable Integer id, @RequestBody BlogPost updatedBlogPost) {
        BlogPost updatedPost = blogPostService.updateBlogPost(id, updatedBlogPost);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Integer id) {
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.noContent().build();
    }
}
