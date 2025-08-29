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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Getter
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Builder
    public InterestStore (
        Long userId,
        Long storeId
    ) {
        this.userId = userId;
        this.storeId = storeId;
    }

    public boolean isExist(Long userId) {
        if(userId == this.userId){
            return true;
        } else return false;
    }
}
