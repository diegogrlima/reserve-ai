package com.github.diegogrlima.reservaai.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "room_number", nullable = false, length = 15, unique = true)
    private String roomNumber;

    @NotBlank
    @Column(name = "room_type", length = 20, nullable = false)
    private String roomType;

    @NotNull
    @Positive
    @Column(name = "daily_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Lob
    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "image", length = 500)
    private String image;

    @Lob
    @Column(name = "gallery", columnDefinition = "JSON")
    private String gallery;

    @Lob
    @Column(name = "amenities", columnDefinition = "JSON")
    private String amenities;
}
