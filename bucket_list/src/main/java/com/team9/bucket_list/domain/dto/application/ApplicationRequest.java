package com.team9.bucket_list.domain.dto.application;

import lombok.*;

@Builder
@Getter
@Data
@AllArgsConstructor
public class ApplicationRequest {

    private String content;
    private Long postId;
}
