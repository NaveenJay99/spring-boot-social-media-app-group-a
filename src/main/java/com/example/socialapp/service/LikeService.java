package com.example.socialapp.service;

import com.example.socialapp.dto.LikeDto;
import com.example.socialapp.entity.Like;
import com.example.socialapp.entity.Post;
import com.example.socialapp.entity.User;
import com.example.socialapp.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    
    private final LikeRepository likeRepository;
    
    public LikeDto toggleLike(Post post, User user) {
        log.info("Toggling like for post: {} by user: {}", post.getId(), user.getEmail());
        
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        
        if (existingLike.isPresent()) {
            // Unlike the post
            likeRepository.delete(existingLike.get());
            post.decrementLikeCount();
            
            log.info("Post unliked by user: {}", user.getEmail());
            return LikeDto.builder()
                    .postId(post.getId())
                    .userId(user.getId())
                    .liked(false)
                    .likeCount(post.getLikeCount())
                    .build();
        } else {
            // Like the post
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            
            likeRepository.save(like);
            post.incrementLikeCount();
            
            log.info("Post liked by user: {}", user.getEmail());
            return LikeDto.builder()
                    .postId(post.getId())
                    .userId(user.getId())
                    .liked(true)
                    .likeCount(post.getLikeCount())
                    .build();
        }
    }
    
    public boolean isPostLikedByUser(Post post, User user) {
        return likeRepository.existsByUserAndPost(user, post);
    }
    
    public Long getLikeCountForPost(Post post) {
        return likeRepository.countLikesByPost(post);
    }
    
    public List<User> getUsersWhoLikedPost(Post post) {
        return likeRepository.findUsersByPost(post);
    }
    
    public List<Like> getLikesByUser(User user) {
        return likeRepository.findByUser(user);
    }
    
    public List<Like> getLikesByPost(Post post) {
        return likeRepository.findByPost(post);
    }
    
    public void removeLike(User user, Post post) {
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
            post.decrementLikeCount();
            log.info("Like removed by user: {} for post: {}", user.getEmail(), post.getId());
        }
    }
    
    public void deleteAllLikesForPost(Post post) {
        List<Like> likes = likeRepository.findByPost(post);
        likeRepository.deleteAll(likes);
        log.info("All likes deleted for post: {}", post.getId());
    }
    
    public void deleteAllLikesByUser(User user) {
        List<Like> likes = likeRepository.findByUser(user);
        likeRepository.deleteAll(likes);
        log.info("All likes deleted for user: {}", user.getEmail());
    }
    
    public long getTotalLikes() {
        return likeRepository.count();
    }
}