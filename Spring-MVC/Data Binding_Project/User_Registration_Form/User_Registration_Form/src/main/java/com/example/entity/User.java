package com.example.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "Name is required")
    private   String Name;
    @Email(message = "Invailid email")
    private String Email;
    @Min(value = 18,message = "Minimum age should not be less than 18")
    private Integer Age;

}
