package com.phuclq.student.domain;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "pledge_contracts")
@Data
@EqualsAndHashCode(callSuper = true) // Thêm vào vì kế thừa Auditable
public class PledgeContract extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // <-- ID LÀ INTEGER

    @Column(nullable = false, name = "store_id")
    private String storeId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "collateral_id")
    private Long collateralId;
}