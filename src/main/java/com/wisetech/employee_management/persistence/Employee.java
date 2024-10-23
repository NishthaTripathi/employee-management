package com.wisetech.employee_management.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_SEQ")
    @SequenceGenerator(name = "EMP_SEQ", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long employeeId;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "EMPLOYEE_DEPARTMENT",
            joinColumns = @JoinColumn(name = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "DEPARTMENT_ID"))
    @ToString.Exclude
    private Set<Department> departments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Employee employee = (Employee) o;
        return getEmployeeId() != null && Objects.equals(getEmployeeId(), employee.getEmployeeId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}