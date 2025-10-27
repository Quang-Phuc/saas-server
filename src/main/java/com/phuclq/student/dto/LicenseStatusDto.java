package com.phuclq.student.dto; // Adjust package name as needed

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Use LocalDate for dates
import java.util.List;

@Data
@NoArgsConstructor
public class LicenseStatusDto {
    private String status; // "valid", "expired", "not_found"
    private LocalDate expiryDate; // Use LocalDate or String as needed
    private Integer currentStoreCount;
    private Integer currentUserCount;
    private List<StoreInfoDto> stores;
    private List<UserInfoDto> users;

    // Constructor for VALID status
    public LicenseStatusDto(String status, LocalDate expiryDate) {
        this.status = status;
        this.expiryDate = expiryDate;
    }

    // Constructor for EXPIRED status
    public LicenseStatusDto(String status, Integer currentStoreCount, Integer currentUserCount, List<StoreInfoDto> stores, List<UserInfoDto> users) {
        this.status = status;
        this.currentStoreCount = currentStoreCount;
        this.currentUserCount = currentUserCount;
        this.stores = stores;
        this.users = users;
    }

    // Constructor for NOT_FOUND status
    public LicenseStatusDto(String status) {
        this.status = status;
    }
}