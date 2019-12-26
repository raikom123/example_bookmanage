package com.example.bookmanage.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @Column(name = "updated_date_time")
    private LocalDateTime updatedDateTime;

    @Version
    private long version;

    @PrePersist
    public void prePersist() {
        LocalDateTime datetime = LocalDateTime.now();
        createdDateTime = datetime;
        updatedDateTime = datetime;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDateTime = LocalDateTime.now();
    }

}
