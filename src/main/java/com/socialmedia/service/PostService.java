package com.socialmedia.service;

import com.socialmedia.dto.PostDto;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.PostLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserService userService;

    public Post createPost(String content, User author) {
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }

    public Post createPost(PostDto postDto) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User must be authenticated to create a post");
        }
        return createPost(postDto.getContent(), currentUser);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public List<Post> findPostsByAuthor(User author) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    public Page<Post> findPostsByAuthor(User author, Pageable pageable) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author, pageable);
    }

    public List<Post> findPostsByAuthorId(Long authorId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    public Page<Post> findFeedPosts(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Page.empty();
        }
        return postRepository.findFeedPosts(currentUser.getId(), pageable);
    }

    public List<Post> findFeedPosts() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return postRepository.findFeedPosts(currentUser.getId(), Pageable.unpaged()).getContent();
    }

    public Page<Post> searchPosts(String searchTerm, Pageable pageable) {
        return postRepository.findByContentContaining(searchTerm, pageable);
    }

    public Post updatePost(Long postId, String content) {
        Post post = findById(postId);
        User currentUser = userService.getCurrentUser();
        
        if (currentUser == null || !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own posts");
        }
        
        post.setContent(content);
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = findById(postId);
        User currentUser = userService.getCurrentUser();
        
        if (currentUser == null || !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own posts");
        }
        
        postRepository.delete(post);
    }

    public PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthorName(post.getAuthorFullName());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setLikeCount(post.getLikeCount());
        
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            dto.setLikedByCurrentUser(post.isLikedByUser(currentUser));
        }
        
        return dto;
    }

    public List<PostDto> convertToDtoList(List<Post> posts) {
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Page<PostDto> convertToDtoPage(Page<Post> posts) {
        return posts.map(this::convertToDto);
    }

    public int countPostsByUser(User user) {
        return (int) postRepository.countByAuthor(user);
    }

    public long getTotalPostCount() {
        return postRepository.count();
    }

    public List<Post> getRecentPosts() {
        return postRepository.findRecentPosts();
    }

    public Page<Post> getMostLikedPosts(Pageable pageable) {
        return postRepository.findMostLikedPosts(pageable);
    }

    public boolean canUserEditPost(Long postId, User user) {
        Post post = findById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }

    public boolean canUserDeletePost(Long postId, User user) {
        Post post = findById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }
}