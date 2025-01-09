package com.example.routervuebe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Integer id;
    private String username;
}
