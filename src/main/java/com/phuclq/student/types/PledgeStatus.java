package com.phuclq.student.types;

/**
 * Định nghĩa tình trạng cầm đồ (Pledge Status).
 * Đồng bộ với frontend và logic SQL.
 */
public enum PledgeStatus {

    DANG_VAY("Đang vay", "Khoản vay đang hoạt động, chưa trả hết"),
    QUA_HAN("Quá hạn", "Khoản vay đã quá hạn trả"),
    DA_TRA_HET("Đã trả hết", "Khoản vay đã được thanh toán đầy đủ"),
    DA_DONG("Đã đóng", "Hợp đồng đã đóng, không còn hiệu lực");

    private final String displayName;
    private final String description;

    PledgeStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /** Parse an toàn từ chuỗi frontend (tránh lỗi No enum constant) */
    public static PledgeStatus fromString(String value) {
        if (value == null || value.isEmpty()) return DANG_VAY;
        for (PledgeStatus ps : values()) {
            if (ps.name().equalsIgnoreCase(value)) {
                return ps;
            }
        }
        return DANG_VAY;
    }
}
