package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Department;
import ro.dev.trellteam.web.dto.DepartmentDto;

@Mapper
public interface DepartmentMapper {
    Department dtoToDomain(DepartmentDto departmentDto);
    DepartmentDto domainToDto(Department department);
}
