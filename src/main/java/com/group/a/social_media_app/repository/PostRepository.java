package com.group.a.social_media_app.repository;


import com.group.a.social_media_app.entity.Post;
import com.group.a.social_media_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
    List<Post> findByContentContaining(String keyword);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByAuthorInOrderByCreatedAtDesc(List<User> authors);
}