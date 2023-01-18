package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;
    private int cost;
    private String untilRecruit;
    private String entrantNum;
    private String eventStart;
    private String eventEnd;

    @Enumerated(value = EnumType.STRING)
    private PostStatus status;

    @Enumerated(value = EnumType.STRING)
    private PostCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();
}