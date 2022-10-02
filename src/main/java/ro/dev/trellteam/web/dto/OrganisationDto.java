package ro.dev.trellteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDto {
    private Long id;
    private String name;
    private String sign;
    private String CUI;
    private Date dateCreated;
    private String domain;
    private Set<DepartmentDto> departments;
    private Set<EmployeeDto> employees;
}
