package com.group.a.social_media_app.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"}), name = "friend_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FriendRequestStatus status = FriendRequestStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum FriendRequestStatus { PENDING, ACCEPTED, DECLINED }

    public void accept(){
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void decline(){
        this.status = FriendRequestStatus.DECLINED;
    }

    public boolean isPending(){
        return this.status == FriendRequestStatus.PENDING;
    }

    public boolean isAccepted(){
        return this.status == FriendRequestStatus.ACCEPTED;
    }

    public boolean isDeclined(){
        return this.status == FriendRequestStatus.DECLINED;
    }

}