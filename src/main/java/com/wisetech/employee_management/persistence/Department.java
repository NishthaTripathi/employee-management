package com.wisetech.employee_management.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_DEPARTMENT")
    @SequenceGenerator(name = "SEQUENCE_DEPARTMENT", sequenceName = "SEQUENCE_DEPARTMENT", allocationSize = 1)
    @Column(name = "ID")
    private Long departmentId;

    @Column(name = "NAME", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "MANDATORY", nullable = false)
    private Boolean isMandatory = false;

    @Column(name = "READ_ONLY", nullable = false)
    private Boolean isReadOnly = false;

    @ManyToMany(mappedBy = "departments")
    @ToString.Exclude //TODO: Fix
    private Set<Employee> employees;

}