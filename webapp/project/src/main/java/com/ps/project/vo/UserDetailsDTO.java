package com.ps.project.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDetailsDTO {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
