package com.endrezadravecz.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class EmployeeCreationDTO {

    @Size(min = 2)
    private String name;

    @Size(min = 2)
    private String role;

    @NotNull
    private Long managerId;

}
