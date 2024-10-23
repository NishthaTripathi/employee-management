package com.wisetech.employee_management.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPLOYEE")
    @SequenceGenerator(name = "SEQUENCE_EMPLOYEE", sequenceName = "SEQUENCE_EMPLOYEE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME_FIRST", nullable = false)
    private String firstName;

    @Column(name = "NAME_LAST", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "EMPLOYEE_DEPARTMENT",
            joinColumns = @JoinColumn(name = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "DEPARTMENT_ID"))
    private Set<Department> departments;

}