package com.ps.project.vo.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileEditRequest {
    private String title;

    private String description;

    private String url;
}
