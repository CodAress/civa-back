package com.civa.platform.shared.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class AuditableModel {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * Performs logical deletion by setting isActive to false
     */
    public void delete() {
        this.isActive = false;
    }

    /**
     * Restores a logically deleted entity by setting isActive to true
     */
    public void restore() {
        this.isActive = true;
    }

    /**
     * Checks if the entity is logically deleted
     */
    @JsonIgnore
    public boolean isDeleted() {
        return !this.isActive;
    }
}
