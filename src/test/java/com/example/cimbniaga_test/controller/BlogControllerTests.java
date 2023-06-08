package com.example.cimbniaga_test.controller;

import com.example.cimbniaga_test.model.BlogPost;
import com.example.cimbniaga_test.service.BlogPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(BlogPostController.class)
public class BlogControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogPostService blogPostService;

    @InjectMocks
    private BlogPostController blogController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBlogPost() throws Exception {
        BlogPost blogPost = new BlogPost("My First Blog Post", "This is the content", "John Doe");
        when(blogPostService.createBlogPost(any(BlogPost.class))).thenReturn(blogPost);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/blog-posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"My First Blog Post\",\"body\":\"This is the content\",\"author\":\"John Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("My First Blog Post"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("This is the content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("John Doe"));
    }
}
