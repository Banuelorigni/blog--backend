package com.example.blog.infrastructure;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.Instant;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Access(AccessType.FIELD)
@Getter
@Setter
public abstract class BaseEntity implements Auditable {
    public static final String SKIP_DELETED_CLAUSE = "deleted = false";

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    private Instant updatedAt= Instant.now();

    @CreatedBy
    @Column(updatable = false)
    private String createdBy= "Anonymous";

    @LastModifiedBy
    private String updatedBy= "Anonymous";

    @Version
    private Integer version = 0;

    private Boolean deleted = false;

    @Override
    public Boolean isDeleted() {
        return deleted;
    }
}
