package com.example.cimbniaga_test.service;

import com.example.cimbniaga_test.exception.NotFoundException;
import com.example.cimbniaga_test.model.BlogPost;
import com.example.cimbniaga_test.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public BlogPost createBlogPost(BlogPost blogPost) {

        return blogPostRepository.save(blogPost);
    }

    public Optional<BlogPost> getBlogPostById(Integer id) {
        return blogPostRepository.findById(id);
    }

    public Page<BlogPost> getAllBlogPosts(int page, int size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
        Pageable pageable = PageRequest.of(page, size, sort);
        return blogPostRepository.findAll(pageable);
    }

    public BlogPost updateBlogPost(Integer id, BlogPost updatedBlogPost) {
        Optional<BlogPost> optionalBlogPost = blogPostRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost blogPost = optionalBlogPost.get();
            blogPost.setTitle(updatedBlogPost.getTitle());
            blogPost.setBody(updatedBlogPost.getBody());
            blogPost.setAuthor(updatedBlogPost.getAuthor());
            return blogPostRepository.save(blogPost);
        } else {
            throw new NotFoundException("Blog post not found with id: " + id);
        }
    }

    public void deleteBlogPost(Integer id) {
        blogPostRepository.deleteById(id);
    }
}
