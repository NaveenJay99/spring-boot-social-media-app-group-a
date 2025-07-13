package com.group.a.social_media_app.service;

import com.group.a.social_media_app.dto.PostDto;
import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    
    public Post createPost(String content, User author) {
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(author);
        
        return postRepository.save(post);
    }
    
    public List<PostDto> getUserPosts(User user) {
        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(user);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthorName(post.getAuthor().getFullName());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}