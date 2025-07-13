package com.example.socialapp.service;

import com.example.socialapp.dto.PostDto;
import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import com.example.socialapp.exception.UserNotFoundException;
import com.example.socialapp.repository.PostRepository;
import com.example.socialapp.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    
    private final PostRepository postRepository;
    private final LikeService likeService;
    
    public Post createPost(String content, User author) {
        log.info("Creating post by user: {}", author.getEmail());
        
        Post post = Post.builder()
                .content(content.trim())
                .author(author)
                .likeCount(0)
                .build();
        
        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with id: {}", savedPost.getId());
        return savedPost;
    }
    
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }
    
    public Page<Post> getPostsByCurrentUser(User currentUser, Pageable pageable) {
        return postRepository.findPostsByCurrentUser(currentUser, pageable);
    }
    
    public Page<Post> getHomeFeed(User currentUser, Pageable pageable) {
        Set<User> friends = currentUser.getFriends();
        
        if (friends.isEmpty()) {
            // If no friends, show only current user's posts
            return postRepository.findPostsByCurrentUser(currentUser, pageable);
        } else {
            // Show current user's posts and friends' posts
            return postRepository.findPostsByCurrentUserAndFriends(currentUser, friends, pageable);
        }
    }
    
    public PostDto convertToDto(Post post, User currentUser) {
        boolean isLikedByCurrentUser = likeService.isPostLikedByUser(post, currentUser);
        
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .authorName(post.getAuthor().getFullName())
                .authorId(post.getAuthor().getId())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .likedByCurrentUser(isLikedByCurrentUser)
                .build();
    }
    
    public void deletePost(Long postId, User currentUser) {
        Post post = findById(postId);
        
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own posts");
        }
        
        log.info("Deleting post with id: {} by user: {}", postId, currentUser.getEmail());
        postRepository.delete(post);
    }
    
    public Post updatePost(Long postId, String newContent, User currentUser) {
        Post post = findById(postId);
        
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own posts");
        }
        
        post.setContent(newContent.trim());
        return postRepository.save(post);
    }
    
    public long getPostCountByUser(User user) {
        return postRepository.countPostsByUser(user);
    }
    
    public void updateLikeCount(Post post) {
        Long likeCount = likeService.getLikeCountForPost(post);
        post.setLikeCount(likeCount.intValue());
        postRepository.save(post);
    }
    
    public long getTotalPosts() {
        return postRepository.count();
    }
}