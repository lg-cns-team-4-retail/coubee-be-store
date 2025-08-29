package com.coubee.coubeebestore.domain;

import com.coubee.coubeebestore.domain.dto.StoreUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Setter
    private String storeName;

    @Column(columnDefinition = "text")
    @Setter
    private String description;

    @Column(length = 20)
    @Setter
    private String contactNo;

    private String workingHour;

    @Column(length = 255)
    private String storeAddress;

    private double latitude;
    private double longitude;

    @Column(columnDefinition = "geometry(Point,4326)")
    @JsonIgnore
    private Point location;

    @Column(length = 50)
    private String bizNo;

    @Column(columnDefinition = "text")
    @Setter
    private String backImg;

    @Column(columnDefinition = "text")
    @Setter
    private String profileImg;

    @Column(columnDefinition = "text")
    @Setter
    private String bizImg;

    @Enumerated(EnumType.STRING)
    @Setter
    private StoreStatus status;

    @Setter
    private LocalDateTime approvedAt;

    @Setter
    private String rejectReason;

    @Builder
    public Store(
            Long ownerId,
            String storeName,
            String description,
            String contactNo,
            String storeAddress,
            String workingHour,
            double latitude,
            double longitude,
            String bizNo,
            String backImg,
            String profileImg,
            String bizImg,
            Point location,
            LocalDateTime approvedAt,
            String rejectReason
    ) {
        this.ownerId = ownerId;
        this.storeName = storeName;
        this.description = description;
        this.contactNo = contactNo;
        this.storeAddress = storeAddress;
        this.workingHour = workingHour;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.bizNo = bizNo;
        this.backImg = backImg;
        this.profileImg = profileImg;
        this.bizImg = bizImg;
        this.status = StoreStatus.PENDING;
        this.approvedAt = approvedAt;
        this.rejectReason = rejectReason;
    }

    public void updateStore(StoreUpdateDto storeUpdateDto) {
        this.description = storeUpdateDto.getDescription();
        this.contactNo = storeUpdateDto.getContactNo();
        this.workingHour = storeUpdateDto.getWorkingHour();
        this.backImg = storeUpdateDto.getBackImg();
        this.profileImg = storeUpdateDto.getProfileImg();
    }
}