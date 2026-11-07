package com.dheerx.DheerX_Pay.merchant.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Gatter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class merchant {
    @Id
    private UUID id;




}
