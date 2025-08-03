package com.coubee.coubeebestore.domain;

import com.coubee.coubeebestore.domain.dto.StoreUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Enumerated(EnumType.STRING)
    @Setter
    private StoreStatus status;

    @Setter
    private LocalDateTime approvedAt;

    @Setter
    private String rejectReason;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreCategory> storeCategories = new ArrayList<>();
//    @ElementCollection
//    @Setter
//    private List<String> category = new ArrayList<>();


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
            Point location,
            LocalDateTime approvedAt,
            String rejectReason
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
        this.approvedAt = approvedAt;
        this.rejectReason = rejectReason;
    }

    public void updateStore(StoreUpdateDto storeUpdateDto) {
        this.storeName = storeUpdateDto.getStoreName();
        this.description = storeUpdateDto.getDescription();
        this.contactNo = storeUpdateDto.getContactNo();
        this.storeAddress = storeUpdateDto.getStoreAddress();
        this.latitude = storeUpdateDto.getLatitude();
        this.longitude = storeUpdateDto.getLongitude();
        this.backImg = storeUpdateDto.getBackImg();
        this.profileImg = storeUpdateDto.getProfileImg();
    }

}