package com.group.a.social_media_app.service;


import com.group.a.social_media_app.dto.PostDTO;
import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import com.group.a.social_media_app.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;

    /**
     * Create a new post
     * @param postDTO the post data
     * @param user the user creating the post
     * @return the created post
     */
    public Post createPost(PostDTO postDTO, User user) {
        log.info("Creating post for user: {}", user.getEmail());

        Post post = Post.builder()
                .content(postDTO.getContent())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with ID: {}", savedPost.getId());

        return savedPost;
    }

    /**
     * Get all posts by user
     * @param user the user
     * @return List of posts
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Get posts by user with pagination
     * @param user the user
     * @param pageable pagination information
     * @return Page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * Get posts by user ID
     * @param userId the user ID
     * @return List of posts
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Find post by ID
     * @param id the post ID
     * @return Optional containing the post if found
     */
    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Update post
     * @param post the post to update
     * @return the updated post
     */
    public Post updatePost(Post post) {
        log.info("Updating post with ID: {}", post.getId());
        return postRepository.save(post);
    }

    /**
     * Delete post by ID
     * @param id the post ID
     */
    public void deletePost(Long id) {
        log.info("Deleting post with ID: {}", id);
        postRepository.deleteById(id);
    }

    /**
     * Convert Post entity to PostDTO
     * @param post the post entity
     * @return PostDTO
     */
    public PostDTO convertToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .content(post.getContent())
                .authorName(post.getUser().getFullName())
                .createdAt(post.getCreatedAt())
                .timeAgo(post.getTimeAgo())
                .build();
    }

    /**
     * Convert list of Post entities to PostDTOs
     * @param posts the post entities
     * @return List of PostDTOs
     */
    public List<PostDTO> convertToDTOs(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get post count by user
     * @param user the user
     * @return number of posts
     */
    @Transactional(readOnly = true)
    public long getPostCountByUser(User user) {
        return postRepository.countByUser(user);
    }

    /**
     * Get posts with user information
     * @param userId the user ID
     * @return List of posts with user info
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsWithUser(Long userId) {
        return postRepository.findPostsWithUserByUserId(userId);
    }
}
