package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewResponse;
import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.dto.postFile.DeleteFileResponse;
import com.team9.bucket_list.domain.dto.postFile.UploadFileResponse;
import com.team9.bucket_list.service.BucketlistReviewService;
import com.team9.bucket_list.service.CommentService;
import com.team9.bucket_list.service.PostFileService;
import com.team9.bucket_list.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "버킷리스트(게시글)", description = "게시글 CRUD 기능을 수행합니다. 좋아요, 신청서 수락/거절 기능을 포함합니다.")
public class PostRestController {

    private final PostService postService;
    private final BucketlistReviewService bucketlistReviewService;
    private final PostFileService postFileService;

    private final CommentService commentService;

    // 게시글 폼에서 데이터 받아오기(Ajax 사용하여 받아옴)
    @PostMapping(value = "" ,produces = "application/json")
    @ResponseBody
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    public Response<PostIdResponse> getData(@RequestBody PostCreateRequest request, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;

        log.info("data");
        String userName = "test";
        PostCreateResponse response = postService.create(request, userId);       // DB에 데이터 저장
        log.info("postId():" + response.getPostId());
        PostIdResponse postid = new PostIdResponse(response.getPostId());
        return Response.success(postid);
    }


    //== 검색 기능 ==//
    // 검색 데이터 전송하고 반환
    @GetMapping("/search")
    @ResponseBody
    @Operation(summary = "검색 기능", description = "카테고리, 날짜, 키워드를 입력받아 검색합니다.")
    public Response<Page<PostReadResponse>> searchPost(@Parameter(hidden = true) @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam(value = "category", required = false) String category, @RequestParam(value = "keyword", required = false) String keyword
            , @RequestParam(value = "eventStart", required = false) String eventStart, @RequestParam(value = "eventEnd", required = false) String eventEnd
            , @RequestParam(value = "lowCost", required = false) String lowCost, @RequestParam(value = "upCost", required = false) String upCost) {

        log.info(lowCost);
        log.info(upCost);

        Page<PostReadResponse> response = postService.search(pageable, category, keyword, eventStart, eventEnd, lowCost, upCost);
        return Response.success(response);
    }

    //== 전체조회 ==//
    @GetMapping("")
    @ResponseBody
    @Operation(summary = "게시글 조회", description = "카테고리 별로 게시글 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> readAllPost(@Parameter(hidden = true) @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                        @RequestParam(value = "category", required = false) String category,
                                                        @RequestParam(value = "eventStart", required = false) String eventStart, @RequestParam(value = "eventEnd", required = false) String eventEnd
            , @RequestParam(value = "lowCost", required = false) String lowCost, @RequestParam(value = "upCost", required = false) String upCost) {

        //        if(category == null){
//            Page<PostReadResponse> postReadResponses = postService.readAll(pageable);
//            log.info("PostList 보기 성공");
//            return Response.success(postReadResponses);
//        } else{
//            Page<PostReadResponse> filterPosts = postService.filter(category, pageable);
//            return Response.success(filterPosts);
//        }
        Page<PostReadResponse> response = postService.postList(pageable, category, eventStart, eventEnd, lowCost, upCost);
        return Response.success(response);
    }


    @GetMapping(value = "/{postId}", produces = "application/json")
    @ResponseBody
    @Operation(summary = "특정 게시글 조회", description = "게시글 id를 통해 조회하여 게시글을 출력합니다.")
    public Response<PostReadResponse> jsonreadPost(@Parameter(name = "postId", description = "게시글 id") @PathVariable(value = "postId") Long postId) {
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("DB에서 데이터 호출 location :" + postReadResponse.getLocation());
        return Response.success(postReadResponse);
    }

    // 클라이언트에서 데이터 받아와서 수정
    @PutMapping(value = "/{postId}", produces = "application/json")
    public Response<PostUpdateResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request, Authentication authentication) {
        // update 메서드를 통해 request 내용대로 수정해준다. 반환값 : post Entity
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        log.info(request.toString());
        log.info("postId:" + postId);
        PostUpdateResponse response = postService.update(request, postId, userId);
        log.info("🔵 Post 수정 성공");
        return Response.success(response);
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "postId에 따라 게시글을 삭제합니다.")
    public Response<String> deletePost(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        postService.delete(postId, userId);
        log.info("Post 삭제 성공");
        return Response.success("삭제 완료");
    }


    //== 좋아요 확인 ==//
    @GetMapping("/{postId}/likes")
    @Operation(summary = "좋아요 - 좋아요 여부 확인", description = "로그인 되어 있는 member가 해당 게시글에 좋아요를 눌렀는지 확인합니다. UI에서 좋아요 버튼이 눌려있는것을 표현하기 위해 구현했습니다.")
    public Response<Integer> checkLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,
                                       Authentication authentication) {
        int checkLike = postService.checkLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(checkLike);
    }

    //== 좋아요 개수 ==//
    @GetMapping("/{postId}/likes/count")
    @Operation(summary = "좋아요 - 좋아요 갯수 확인", description = "해당 게시글에 눌린 좋아요 개수를 출력합니다.")
    public Response<Long> countLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId) {
        Long cntLike = postService.countLike(postId);
        return Response.success(cntLike);
    }

    //== 좋아요 누르기 ==//
    @PostMapping("/{postId}/likes")
    @Operation(summary = "좋아요 - 좋아요 작성", description = "해당 게시글에 좋아요를 작성합니다(누릅니다).")
    public Response<Integer> clickLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,
                                       Authentication authentication) {
        int result = postService.clickLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(result);
    }

    // 버킷리스트 리뷰
    @GetMapping("/{postId}/reviews")
    @Operation(summary = "리뷰 조회", description = "특정게시글의 리뷰를 pageable하여 출력합니다.")
    public Response<Page<BucketlistReviewResponse>> reviewList(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId,
                                                               @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 4, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReviewResponse> bucketlistReviewResponses = bucketlistReviewService.list(postId, pageable);
        return Response.success(bucketlistReviewResponses);
    }

    @PostMapping("/reviews")
    @Operation(summary = "리뷰 작성", description = "특정게시글에 리뷰를 작성합니다.")
    public Response<String> reviewCreate(Authentication authentication, @RequestBody BucketlistReviewRequest bucketlistReviewRequest) {
        Long memberId = Long.valueOf(authentication.getName());
        String result = bucketlistReviewService.create(memberId, bucketlistReviewRequest);
        return Response.success(result);
    }

    // S3에 파일 업로드
    @PostMapping(value = "/{postId}/files",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "파일 업로드", description = "해당 게시글에 첨부된 파일을 S3 버킷에 업로드하고, 서버 DB에 해당 파일 S3객체 URL을 저장 합니다.")
    public Response<UploadFileResponse> upload(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") Long postId,
                                               @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        UploadFileResponse response = postFileService.upload(postId, file);
        return Response.success(response);
    }

    // file 삭제
    @DeleteMapping("/{postId}/files/{postFileId}")
    @Operation(summary = "파일 삭제", description = "해당 게시글에 첨부된 파일을 S3 버킷에서 삭제 하고, 서버 DB에 해당 파일 S3객체 URL을 삭제 합니다.")
    public Response<DeleteFileResponse> delete(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") Long postId,
                                               @Parameter(name = "postFileId", description = "파일 id") @PathVariable("postFileId") Long postFileId) {
        DeleteFileResponse response = postFileService.delete(postId, postFileId);
        return Response.success(response);
    }


    // 댓글


    // 댓글 작성
    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "id를 이용하여 user 레코드를 조회합니다.")
    public Response<CommentCreateResponse> commentCreate(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long id,
                                                         @RequestBody CommentCreateRequest request, Authentication authentication){
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        log.info("댓글작성 username :"+userId);
        return Response.success(commentService.commentCreate(id,request,userId));
    }

    // 댓글 리스트 전체 출력
    @GetMapping("/{postId}/comments")
    @Operation(summary = "댓글 리스트 조회", description = "특정 게시글의 댓글 리스트를 출력합니다.")
    public Response<List<CommentListResponse>> commentList(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId") Long id){
        List<CommentListResponse> commentList = commentService.commentList(id);
        return Response.success(commentList);
    }

    // 댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    public Response<List<CommentListResponse>> commentUpdate(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long postid,
                                                             @Parameter(name = "commentId", description = "댓글 id") @PathVariable(name="commentId")Long id,
                                                             @RequestBody CommentCreateRequest request,Authentication authentication){
        Long memberId = Long.valueOf(authentication.getName());
//        Long memberId = 1l;
        List<CommentListResponse> commentList = commentService.updateComment(postid,id,request,memberId);
        return Response.success(commentList);
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comments/{commentId}")
    @ResponseBody
    public Response<List<CommentListResponse>> commentDelete(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long postid,
                                                             @Parameter(name = "commentId", description = "댓글 id") @PathVariable(name="commentId")Long id
            ,Authentication authentication){
        log.info("댓글 삭제");
        Long memberId = Long.valueOf(authentication.getName());
//        Long memberId = 1l;
        List<CommentListResponse> commentList = commentService.deleteComment(postid,id,memberId);
        return Response.success(commentList);
    }

}
