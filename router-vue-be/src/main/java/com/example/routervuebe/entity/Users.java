package com.example.routervuebe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Entity
@Data
@Table(name="users")

public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    private String pass;
    @NotBlank
    private String gioitinh;
    @NotBlank
    private String diachi;
    @NotBlank
    private String sdt;
    private String image;
    private String email;
    public Users() {
    }


}
