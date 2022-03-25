package com.endrezadravecz.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ManagerCreationDTO {

    @Size(min = 2)
    private String name;

}
