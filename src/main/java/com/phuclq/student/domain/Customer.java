package com.phuclq.student.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "customer")
public class Customer extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId; // thuộc cửa hàng nào

    // ===== Thông tin cơ bản =====
    @Column(name = "customer_code")
    private String customerCode; // Mã khách hàng

    @Column(name = "full_name")
    private String fullName; // Họ tên

    @Column(name = "phone_number")
    private String phoneNumber; // Số điện thoại

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; // Ngày sinh

    @Column(name = "identity_number")
    private String identityNumber; // Số GTTT (CMND/CCCD/Hộ chiếu)

    @Column(name = "issue_date")
    private LocalDate issueDate; // Ngày cấp

    @Column(name = "issue_place")
    private String issuePlace; // Nơi cấp

    @Column(name = "permanent_address", columnDefinition = "LONGTEXT")
    private String permanentAddress; // Địa chỉ thường trú

    @Column(name = "household_registration", columnDefinition = "LONGTEXT")
    private String householdRegistration; // Hộ khẩu thường trú

    @Column(name = "gender")
    private String gender; // Giới tính

    @Column(name = "email")
    private String email; // Email

    @Column(name = "occupation")
    private String occupation; // Nghề nghiệp

    @Column(name = "workplace")
    private String workplace; // Nơi làm việc

    @Column(name = "income_vnd_per_month")
    private BigDecimal incomeVndPerMonth; // Thu nhập (VNĐ/tháng)

    @Column(name = "contact_person")
    private String contactPerson; // Người liên hệ

    @Column(name = "contact_phone")
    private String contactPhone; // Số điện thoại người liên hệ

    @Column(name = "note", columnDefinition = "LONGTEXT")
    private String note; // Ghi chú

    // ===== Thành phần gia đình =====
    @Column(name = "spouse_name")
    private String spouseName; // Vợ/Chồng - Họ tên

    @Column(name = "spouse_phone")
    private String spousePhone; // Vợ/Chồng - Số điện thoại

    @Column(name = "spouse_occupation")
    private String spouseOccupation; // Vợ/Chồng - Nghề nghiệp

    @Column(name = "father_name")
    private String fatherName; // Bố - Họ tên

    @Column(name = "father_phone")
    private String fatherPhone; // Bố - Số điện thoại

    @Column(name = "father_occupation")
    private String fatherOccupation; // Bố - Nghề nghiệp

    @Column(name = "mother_name")
    private String motherName; // Mẹ - Họ tên

    @Column(name = "mother_phone")
    private String motherPhone; // Mẹ - Số điện thoại

    @Column(name = "mother_occupation")
    private String motherOccupation; // Mẹ - Nghề nghiệp

    // ===== Thông tin cho vay / theo dõi khách hàng =====
    @Column(name = "loan_status")
    private String loanStatus; // Tình trạng

    @Column(name = "partner_type")
    private String partnerType; // Loại đối tác

    @Column(name = "customer_type")
    private String customerType; // Khách hàng

    @Column(name = "follower")
    private String follower; // Người theo dõi

    @Column(name = "customer_source")
    private String customerSource; // Nguồn khách hàng

    @Column(length = 1024, name = "id_url")
    private String idUrl; // URL ảnh chân dung

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

}
