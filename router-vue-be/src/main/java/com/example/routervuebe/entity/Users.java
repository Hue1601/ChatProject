package com.example.routervuebe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor

public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String pass;
    private String gioitinh;
    private String diachi;
    private String sdt;
    private String image;
    private String email;



}
