package pl.grabowski.studentmanager.utils;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.grabowski.studentmanager.controller.student.StudentUpdateRequest;
import pl.grabowski.studentmanager.model.student.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentUpdateRequest dto, @MappingTarget Student student);
}
