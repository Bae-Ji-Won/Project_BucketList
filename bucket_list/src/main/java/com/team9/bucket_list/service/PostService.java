package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.entity.Likes;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.ApplicationRepository;
import com.team9.bucket_list.repository.LikesRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import com.team9.bucket_list.utils.map.dto.GoogleMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final ApplicationRepository applicationRepository;
    private final AlarmService alarmService;
    // map
    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    // ========= 유효성검사 ===========
    // 1. findByMemberId : memberId로 member 찾아 로그인 되어있는지 확인 ->  error : 권한이 없습니다.
    public Member checkMember(String userName) {
        return memberRepository.findByUserName(userName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }
//    // 2. checkPostMember : post를 작성한 mem과 현재 로그인 된 mem 비교 -> INVALID_PERMISSION
//    public void checkPostMember(Long memberId, Long postMemberId) {
//        Member loginMember = checkMember(memberId);
//        Member postMember = checkMember(postMemberId);
//        if(!loginMember.equals(postMember)) throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
//    }
    // 3. findByPostId : postId에 따른 post가 DB에 잘 있는지 확인 -> error : 없는 게시물입니다. POST_NOT_FOUND
    public Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    // 작성
    @Transactional
    public PostCreateResponse create(PostCreateRequest request, String username) {
        // 로그인 되어있는지 확인하고 아니면 에러던짐
//        Member member = checkMember(username);
        // requestDTO 안의 toEntity 메서드를 이용해 post Entity 객체를 생성한다.
        Post post = request.toEntity();
        // request를 통해 만들어진 post를 DB에 저장한다.
        Post savedPost = postRepository.save(post);
        return PostCreateResponse.of(savedPost);
    }

    // 전체 조회 (page)
    public Page<PostReadResponse> readAll(Pageable pageable) {
        // Entity
        Page<Post> posts = postRepository.findAll(pageable);
        // Dto
        Page<PostReadResponse> postReadResponses = PostReadResponse.listOf(posts);
        return postReadResponses;
    }

    // 상세조회
    public PostReadResponse read(Long postId) {
        // Entity
        Post post = checkPost(postId);
        // Dto
        Map<String, Double> locationNum = findLoction(post.getLocation());
        Double lat = locationNum.get("lat");
        Double lng = locationNum.get("lng");
        PostReadResponse postReadResponse = PostReadResponse.detailOf(post,lat,lng);
        return postReadResponse;
    }

    // 내가 쓴 글만 조회 (마이피드)
    // return을 뭘로 해야될지 모르겠음.. 나중에 마이페이지 생길 때까지 보류
    public Page<PostReadResponse> findMine(Long memberId, Pageable pageable) {
        return null;
    }

    // 수정
    @Transactional
    public void update(PostUpdateRequest request, Long postId) {
//        로그인 되어있는지 확인하고 아니면 에러던짐
//        Member member = checkMember(memberId);
//        post를 쓴 멤버와 로그인 되어 있는 member가 같은 멤버가 아니면 에러던짐
//        checkPostMember(memberId, post.getId());

        // postid에 해당하는 post가 DB에 없으면 에러던짐 - entity
        Post post = checkPost(postId);

        log.info("🔴 post : {}", post.toString());
        log.info("🔴 post : {}", request.toString());
        // 수정 사항을 반영하여 변경한다.
        postRepository.save(Post.update(post, request));
    }

    // 삭제
    @Transactional
    public PostDeleteResponse delete(Long postId, Long memberId) {
        // 로그인 되어있는지 확인하고 아니면 에러던짐
//        Member member = checkMember(memberId);
        // postid에 해당하는 post가 DB에 없으면 에러던짐
        Post post = checkPost(postId);
        // post를 쓴 멤버와 로그인 되어 있는 member가 같은 멤버가 아니면 에러던짐
//        checkPostMember(memberId, post.getId());
        postRepository.deleteById(post.getId());
        return PostDeleteResponse.of(post);

    }

    // map API
    // 회원가입에서 입력받은 주소의 위/경도 좌표를 구하기 위해 구글 api 사용
    public Map<String,Double> findLoction(String location){
        UriComponents uri = UriComponentsBuilder.newInstance()          // UriComponentsBuilder.newInstance = uri 주소를 직접 조립하여 만든다
                // https://maps.googleapis.com/maps/api/geocode/json?address="address"&key="API_KEY"와 같음
                // 위 처럼 한번에 사용하지 않고 조립해서 사용하는 이유는 address나 key값처럼 외부에서 값을 받아올때 쉽게 넣어 조립이 가능하기 때문
                .scheme("https")
                .host("maps.googleapis.com")
                .path("/maps/api/geocode/json")
                .queryParam("key",API_KEY)
                .queryParam("address",location)
                .build();
        System.out.println("MapService url : "+uri.toUriString());


        GoogleMapResponse response = new RestTemplate().getForEntity(uri.toUriString(), GoogleMapResponse.class).getBody();     // 구글 map api에서 반환해주는 json형식을 MapResponse클래스 형식에 맞춰 받아옴
        Double lat = Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLat();
        Double lng =Arrays.stream(response.getResult()).findFirst().get().getGeometry().getLocation().getLng();
        Map<String,Double> locationNum = new HashMap<>();
        locationNum.put("lat", lat);
        locationNum.put("lng", lng);

        return locationNum;
    }

    //== 좋아요 ==//
    // 좋아요 개수
    public Long countLike(Long postId) {
        // postid에 해당하는 post가 DB에 없으면 에러던짐 - entity
        checkPost(postId);

        return likesRepository.countByPostId(postId);
    }

    // 좋아요 누르기
    @Transactional
    public int clickLike(long postId, String userName) {
        //        로그인 되어있는지 확인하고 아니면 에러던짐 -> userName인지 memberId인지 확인하여 수정
        Member member = checkMember(userName);

        // postid에 해당하는 post가 DB에 없으면 에러던짐 - entity
        Post post = checkPost(postId);

        Optional<Likes> savedLike = likesRepository.findByPost_IdAndMember_Id(postId, member.getId());

        if (savedLike.isEmpty()){
            Likes likes = Likes.builder()
                    .member(member)
                    .post(post)
                    .build();
            likesRepository.save(likes);

            // 좋아요 됐을 경우, 알람 DB에 추가
            alarmService.sendAlarm(member.getId(), post.getId(), (byte) 1);
            return 1;
        }else {
            likesRepository.deleteByPost_IdAndMember_Id(postId, member.getId());
            return 0;
        }
    }

    //== 마이피드 ==//
    // 작성한 포스트 리턴
    public Page<PostReadResponse> myFeedCreate(Pageable pageable, String userName) {
        //        로그인 되어있는지 확인하고 아니면 에러던짐 -> userName인지 memberId인지 확인하여 수정
        Member member = checkMember(userName);

        // Entity
        Page<Post> createPosts = postRepository.findByMember_Id(member.getId(), pageable);
        // Dto
        Page<PostReadResponse> createPostReadResponses = PostReadResponse.listOf(createPosts);

        return createPostReadResponses;
    }

    // 신청한 포스트 리턴
    public Page<PostReadResponse> myFeedApply(Pageable pageable, String userName) {
        //        로그인 되어있는지 확인하고 아니면 에러던짐 -> userName인지 memberId인지 확인하여 수정
        Member member = checkMember(userName);

        // Entity
        Set<Application> applications = applicationRepository.findByMember_Id(member.getId());
        Set<Long> postIds = applications.stream().map(Application::getPost).map(Post::getId).collect(Collectors.toSet());
        Page<Post> applyPosts = postRepository.findByIdIn(postIds, pageable);
        // Dto
        Page<PostReadResponse> applyPostReadResponses = PostReadResponse.listOf(applyPosts);

        return applyPostReadResponses;
    }

    // 좋아요한 포스트 리턴
    public Page<PostReadResponse> myFeedLike(Pageable pageable, String userName) {
        //        로그인 되어있는지 확인하고 아니면 에러던짐 -> userName인지 memberId인지 확인하여 수정
        Member member = checkMember(userName);

        // Entity
        Set<Likes> likes = likesRepository.findByMember_Id(member.getId());
        Set<Long> postIds = likes.stream().map(Likes::getPost).map(Post::getId).collect(Collectors.toSet());
        Page<Post> likePosts = postRepository.findByIdIn(postIds, pageable);
        // Dto
        Page<PostReadResponse> likePostReadResponses = PostReadResponse.listOf(likePosts);

        return likePostReadResponses;
    }
}