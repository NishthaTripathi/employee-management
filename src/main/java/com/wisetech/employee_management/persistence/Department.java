package com.wisetech.employee_management.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEP_SEQ")
    @SequenceGenerator(name = "DEP_SEQ", sequenceName = "DEP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long departmentId;

    @Column(name = "NAME", nullable = false)
    private String departmentName;

    @Column(name = "MANDATORY", nullable = false)
    private Boolean isMandatory = false;

    @Column(name = "READ_ONLY", nullable = false)
    private Boolean isReadOnly = false;

    @ManyToMany(mappedBy = "departments")
    @ToString.Exclude
    private Set<Employee> employees;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Department that = (Department) o;
        return getDepartmentId() != null && Objects.equals(getDepartmentId(), that.getDepartmentId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}