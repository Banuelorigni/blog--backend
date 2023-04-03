package com.blog.infrastructure;

import java.time.Instant;

public interface Auditable {

    Instant getCreatedAt();

    Instant getUpdatedAt();

    String getCreatedBy();

    String getUpdatedBy();

    Boolean isDeleted();

    void setDeleted(Boolean deleted);
}
