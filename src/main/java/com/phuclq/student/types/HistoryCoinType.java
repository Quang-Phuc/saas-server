package com.phuclq.student.types;


public enum HistoryCoinType {
    DOWNLOAD_FILE(1, "Tải tài liệu", "-"),
    DOWNLOADED_FILE(2, "Tài liệu được tải", "+"),
    PAY_COIN(3, "Nạp tiền vào tài khoản", "+"),
    PAYMENT_DONE(4, "Rút tiền thành công", "-"),
    DOWNLOAD_FILE_ADMIN(5, "Tải tài liệu thành cộng cho admin", "+"),
    REGISTER_VIP(6, "Đăng ký tài liệu vip", "-"),
    SEO_FILE(7, "Seo tin file", "-"),
   FILE_UPLOADED(8, "Tài liệu của bạn được duyệt", "+");


    private final Integer code;
    private final String name;
    private final String type;

    HistoryCoinType(Integer code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
