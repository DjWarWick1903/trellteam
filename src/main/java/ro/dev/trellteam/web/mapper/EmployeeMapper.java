package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.web.dto.EmployeeDto;

@Mapper
public interface EmployeeMapper {
    Employee dtoToDomain(EmployeeDto employeeDto);
    EmployeeDto domainToDto(Employee employee);
}
