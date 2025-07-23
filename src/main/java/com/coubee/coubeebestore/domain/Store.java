package com.coubee.coubeebestore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    private Long ownerId;

    @Column(nullable = false, length = 255)
    private String storeName;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 20)
    private String contactNo;

    @Column(length = 255)
    private String storeAddress;

    private double latitude;
    private double longitude;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(length = 50)
    private String bizNo;

    @Column(columnDefinition = "text")
    private String backImg;

    @Column(columnDefinition = "text")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private LocalDateTime approvedAt;

    private String rejectReason;

    @Builder
    public Store(
            Long ownerId,
            String storeName,
            String description,
            String contactNo,
            String storeAddress,
            double latitude,
            double longitude,
            String bizNo,
            String backImg,
            String profileImg,
            Point location
    ) {
        this.ownerId = ownerId;
        this.storeName = storeName;
        this.description = description;
        this.contactNo = contactNo;
        this.storeAddress = storeAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.bizNo = bizNo;
        this.backImg = backImg;
        this.profileImg = profileImg;
        this.status = StoreStatus.PENDING;
    }

}