package com.phuclq.student.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RENTING_HOUSE")
@Getter
@Setter
@Builder
@Table(name = "RENTING_HOUSE")
public class RentalHouse extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "RENTING_HOUSE_SEQUENCE", sequenceName = "RENTING_HOUSE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENTING_HOUSE_SEQUENCE")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "id_province", nullable = false)
    private Integer idProvince;

    @Column(name = "id_district", nullable = false)
    private Integer idDistrict;

    @Column(name = "id_ward", nullable = false)
    private Integer idWard;

    @Column(name = "id_street", nullable = false)
    private Integer idStreet;

    @Column(name = "ADDRESS", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "number_beds", nullable = false)
    private Integer numberBeds;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "number_bathroom", nullable = false)
    private Integer numberBathroom;

    @Column(name = "closed", nullable = false)
    private Boolean closed;

    @Column(name = "shared_room", nullable = false)
    private Boolean sharedRoom;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "acreage", nullable = false)
    private Integer acreage;

    @Column(name = "number_toilet ")
    private Integer numberToilet;
}
