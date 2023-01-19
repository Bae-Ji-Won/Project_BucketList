package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class PostReadResponse {
    private Long postId;
    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private String untilRecruit; //모집종료날짜
    private String entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private PostStatus status; //defalt = 모집중
    private PostCategory category; //카테고리
    private Member member; // 버킷리스트를 만든 member --> member_id, nickname
    private List<Application> applicationList; // 버킷리스트 참가자 목록
    private List<Likes> likeList; // 버킷리스트 좋아요 누른 사람 목록
    private List<Comment> commentList; // 버킷리스트의 댓글들


    // 글 하나 상세볼 때 사용하는 메서드
    public static PostReadResponse detailOf(Post post) {
        return PostReadResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .cost(post.getCost())
                .location(post.getLocation())
                .untilRecruit(post.getUntilRecruit())
                .entrantNum(post.getEntrantNum())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .status(post.getStatus())
                .category(post.getCategory())
                .member(post.getMember())
                .applicationList(post.getApplicationList())
                .likeList(post.getLikesList())
                .commentList(post.getCommentList())
                .build();
    }

    // list로 볼 때 사용하는 메서드
    public static Page<PostReadResponse> listOf(Page<Post> posts) {
        return posts.map(post -> PostReadResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .member(post.getMember())
                .status(post.getStatus())
                .untilRecruit(post.getUntilRecruit())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .applicationList(post.getApplicationList()) // 총 승인 인원 확인
                .build()
        );
    }
}