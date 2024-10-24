package com.wisetech.employee_management.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPLOYEE")
    @SequenceGenerator(name = "SEQUENCE_EMPLOYEE", sequenceName = "SEQUENCE_EMPLOYEE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotBlank( message = "First Name can not be null or empty")
    @Column(name = "NAME_FIRST", nullable = false)
    private String firstName;

    @NotBlank( message = "Last Name can not be null or empty")
    @Column(name = "NAME_LAST", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "EMPLOYEE_DEPARTMENT",
            joinColumns = @JoinColumn(name = "ID_EMPLOYEE"), inverseJoinColumns = @JoinColumn(name = "ID_DEPARTMENT"))
    private Set<Department> departments;

}