package com.dheerxpay.common.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AuditableDto {

    @Size(max = 100, message = "Created by must not exceed 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Updated by must not exceed 100 characters")
    private String updatedBy;

}
