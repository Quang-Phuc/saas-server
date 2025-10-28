package com.phuclq.student.types;


/**
 * Trạng thái của lịch sử license
 */
public enum LicenseHistoryStatus {

    ACTIVE(1, "Đang hoạt động"),
    EXPIRED(2, "Đã hết hạn"),
    RENEWED(3, "Đã gia hạn"),
    CANCELED(4, "Đã hủy"),
    PENDING_RENEWAL(5, "Chờ gia hạn");

    private final int code;
    private final String description;

    LicenseHistoryStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /** Lấy enum theo code (dùng khi đọc DB) */
    public static LicenseHistoryStatus fromCode(Integer code) {
        if (code == null) return null;
        for (LicenseHistoryStatus status : LicenseHistoryStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tồn tại trạng thái license history với code = " + code);
    }
}

