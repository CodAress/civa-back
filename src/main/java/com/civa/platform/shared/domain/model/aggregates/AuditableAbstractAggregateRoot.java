package com.civa.platform.shared.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>> extends AbstractAggregateRoot<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
