package com.group.a.social_media_app.service;


import com.group.a.social_media_app.dto.PostDTO;
import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(PostDTO dto, User user) {
        log.info("Creating post for: {}", user.getEmail());
        Post post = Post.builder()
                .content(dto.getContent())
                .user(user)
                .build();
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public PostDTO convertToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .content(post.getContent())
                .authorName(post.getUser().getFullName())
                .createdAt(post.getCreatedAt())
                .timeAgo(post.getTimeAgo())
                .build();
    }

    public List<PostDTO> convertToDTOs(List<Post> posts) {
        return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getPostCountByUser(User user) {
        return postRepository.countByUser(user);
    }
}
