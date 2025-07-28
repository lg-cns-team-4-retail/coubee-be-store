package com.coubee.coubeebestore.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_stores", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "store_id"})
})
@NoArgsConstructor
public class InterestStore {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Getter
    private Long storeId;

    @Builder
    public InterestStore (
        Long userId,
        Long storeId
    ) {
        this.userId = userId;
        this.storeId = storeId;
    }
}
