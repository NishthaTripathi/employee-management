package com.wisetech.employee_management.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_DEPARTMENT")
    @SequenceGenerator(name = "SEQUENCE_DEPARTMENT", sequenceName = "SEQUENCE_DEPARTMENT", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotBlank( message = "Name can not be null or empty")
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @NotNull(message = "Mandatory can not be null")
    @Column(name = "MANDATORY", nullable = false)
    private Boolean mandatory = false;

    @NotNull(message = "Read_Only can not be null")
    @Column(name = "READ_ONLY",nullable = false)
    private Boolean readOnly = false;

    @ManyToMany(mappedBy = "departments")
    @ToString.Exclude
    @JsonIgnore
    private Set<Employee> employees;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Department that = (Department) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}