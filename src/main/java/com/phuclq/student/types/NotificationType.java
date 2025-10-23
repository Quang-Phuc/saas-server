package com.phuclq.student.types;

public enum NotificationType {
    PAYMENT_DONE(1, "RÚT TIỀN", "PAYMENT_DONE", "Yều cầu rút tiền của bạn đã được duyệt và đã được rút thành công , vui lòng kiểm tra tài khoản bạn đã đưng ký", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02124654_131_approved.png", "/user/coins"),
    PAYMENT_REJECT(2, "RÚT TIỀN", "PAYMENT_REJECT", "Yều cầu rút tiền của bạn đã bị hủy , vui lòng kiểm tra lại thông tin chuyển khoản", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125143_131_reject.png", "/user/payment"),
    PAYMENT_COMMENT(3, "RÚT TIỀN", "PAYMENT_COMMENT", "Bạn có phản hồi khi thực hiện rút tiền", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125243_131_comment.png", "/user/payment"),
    DOWNLOADED(4, "TÀI LIỆU ĐƯỢC TẢI", "DOWNLOADED", "Tài liệu của bạn được tải", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125349_131_download.png", "/user/document"),
    FILE_ACCEPTED(5, "TÀI LIỆU ĐÃ ĐƯƠỢC DUYỆT", "FILE_ACCEPTED", "Tài liệu của bạn đã được duyệt", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125431_131_agreement.png", "/user/document"),
    HOME_ACCEPTED(6, "TIN CHIA SẺ PHÒNG ĐÃ ĐƯƠỢC DUYỆT", "HOME_ACCEPTED", "Bản tin chia sẻ phòng trọ đã được duyệt", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125431_131_agreement.png", "/user/document"),
    JOB_ACCEPTED(7, "TIN TUYỂN DỤNG ĐÃ ĐƯƠỢC DUYỆT", "JOB_ACCEPTED", "Bản tin tuyển dụng đã được duyệt", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125431_131_agreement.png", "/user/document"),
    CV_ACCEPTED(8, "TIN CHIA SẺ PHÒNG ĐÃ ĐƯƠỢC DUYỆT", "HOME_ACCEPTED", "Bản tin chia sẻ phòng trọ đã được duyệt", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125431_131_agreement.png", "/user/document"),
    BLOG_ACCEPTED(9, "TIN CHIA SẺ PHÒNG ĐÃ ĐƯƠỢC DUYỆT", "HOME_ACCEPTED", "Bản tin chia sẻ phòng trọ đã được duyệt", "https://mbal-bpm-dev.s3.ap-southeast-1.amazonaws.com/public/_2023-10-02125431_131_agreement.png", "/user/document");

    private final Integer code;
    private final String title;
    private final String type;
    private final String message;
    private final String imageIcon;
    private final String urlDetail;

    NotificationType(Integer code, String title, String type, String message, String imageIcon, String urlDetail) {

        this.code = code;
        this.title = title;
        this.type = type;
        this.message = message;
        this.imageIcon = imageIcon;
        this.urlDetail = urlDetail;
    }

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public String getUrlDetail() {
        return urlDetail;
    }
}
