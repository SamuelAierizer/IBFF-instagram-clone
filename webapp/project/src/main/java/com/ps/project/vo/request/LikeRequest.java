package com.ps.project.vo.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikeRequest {
    private String username;
    private Long postId;
}
