package com.ps.project.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String caption;
    private String name;
    private String username;
    private Integer likes;
    private String imageData;
}
