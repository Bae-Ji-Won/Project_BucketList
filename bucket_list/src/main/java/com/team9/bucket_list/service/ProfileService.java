package com.team9.bucket_list.service;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team9.bucket_list.config.AwsS3Config;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.domain.entity.Profile;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.MemberReviewRepository;
import com.team9.bucket_list.repository.comment.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;
    private final MemberReviewRepository memberReviewRepository;
    private final ProfileRepository profileRepository;


    // ========== 유효성 검사 ==========
    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }

    public void checkAuthority(Long memberId, Long profileMemberId) {
        Member loginMember = checkMember(memberId);
        Member profileMember = checkMember(profileMemberId);
        if(loginMember.getUserName().equals(profileMember.getUserName()))
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
    }


    // ========== detail, update ==========
    public List<ProfileResponse> detail(Long targetMemberId) {
        Member member = checkMember(targetMemberId);
        log.info("service의 detail입니다.");

        // 평점 평균
        // 람다로 표현 할 방법은 없을까?
        double avg = 0;
        List<MemberReview> memberReviewList = memberReviewRepository.findAllByMember_Id(targetMemberId);
        for (MemberReview m : memberReviewList) {
            avg += m.getRate();
        }
        avg = (avg / memberReviewList.size());
        avg = Math.round(avg * 100) / 100.0;

        member.rateUpdate(avg);

        List<Member> memberProfile = new ArrayList<>();

        memberProfile.add(member);

        List<ProfileResponse> memberDetailList = ProfileResponse.response(memberProfile, avg);

        return memberDetailList;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String uploadFolder;

    @Transactional
    public ProfileResponse update(Long memberId, String userName, MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.FILE_NOT_EXISTS);
        }

        Member member = checkMember(memberId);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // 사용자가 올린 파일 이름
        String uploadFileName = multipartFile.getOriginalFilename();

        int index;

        try {
            index = uploadFileName.lastIndexOf(".");
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApplicationException(ErrorCode.WRONG_FILE_FORMAT);
        }

        if (!member.getUserName().equals(userName)) {
            new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        String ext = uploadFileName.substring(index + 1);

        String awsS3FileName = UUID.randomUUID() + "." + ext;

        String key = "profileImage/" + awsS3FileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(uploadFolder, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        // db에 저장하기
        String fileUrl = amazonS3Client.getUrl(uploadFolder, key).toString();
        log.info("🔵 amazonS3Client.getUrl = {} ", fileUrl );
        // PostFile postFile = PostFile.save(uploadFileName, fileUrl, post);
        Profile profile = Profile.save(uploadFileName, key, member);
        profileRepository.save(profile);
        log.info("🔵 파일 등록 완료 ");

        return ProfileResponse.updateProfileImage(uploadFileName, awsS3FileName);
    }




}
