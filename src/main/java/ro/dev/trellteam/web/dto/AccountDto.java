package ro.dev.trellteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Date dateCreated;
    private Integer disabled;
    private EmployeeDto employee;
    private List<RoleDto> roles;
}
