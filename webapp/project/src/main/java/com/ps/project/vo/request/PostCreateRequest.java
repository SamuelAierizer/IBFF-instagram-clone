package com.ps.project.vo.request;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostCreateRequest {
    String caption;

    @NotBlank
    @Lob
    String file;
}
