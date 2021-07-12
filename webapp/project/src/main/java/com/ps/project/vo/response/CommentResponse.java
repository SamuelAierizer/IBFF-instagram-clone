package com.ps.project.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private String username;
    private String text;
}
