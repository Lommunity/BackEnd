package com.Lommunity.domain.region;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "regions")
@ToString
public class Region {

    @Id
    @Column(name = "code")
    private Long code; // region code
    private Long parentCode;
    private Long level;
    private String fullName;
}
