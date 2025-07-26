package com.coubee.coubeebestore.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;

@Entity
@Table(name = "interest_stores", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "store_id"})
})
public class InterestStore {
    
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Builder
    public InterestStore (
        Long id,
        Long userId,
        Store store
    ) {
        this.id = id;
        this.userId = userId;
        this.store = store;
    }
}
