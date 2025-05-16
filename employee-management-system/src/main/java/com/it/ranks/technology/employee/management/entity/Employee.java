package com.it.ranks.technology.employee.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Department is mandatory")
    private String department;

    @NotNull(message = "Salary is mandatory")
    @PositiveOrZero
    private BigDecimal salary;
}
