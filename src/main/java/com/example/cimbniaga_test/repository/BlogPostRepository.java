package com.example.cimbniaga_test.repository;

import com.example.cimbniaga_test.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost,Integer> {

}
