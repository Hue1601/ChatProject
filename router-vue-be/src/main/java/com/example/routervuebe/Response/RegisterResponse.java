package com.example.routervuebe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterResponse {
    private String username;
    private String sdt;
    private String email;
    private String gioitinh;
}
