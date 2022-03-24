package com.endrezadravecz.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeCreationDTO {

    private String name;

    private String role;

    private Long managerId;

}
