package com.example.routervuebe.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Integer id;
    private String username;
}
