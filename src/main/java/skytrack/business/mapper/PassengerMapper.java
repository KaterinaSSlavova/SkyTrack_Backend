package skytrack.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import skytrack.dto.user.PassengerRequest;
import skytrack.dto.user.PassengerResponse;
import skytrack.persistence.enumeration.Gender;
import skytrack.persistence.entity.PassengerEntity;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    @Mapping(target="gender", expression="java(entity.getGender().name())")
    PassengerResponse toResponse(PassengerEntity entity);

    PassengerEntity toEntity(PassengerRequest request);

    default Gender map(String gender){
        return Gender.valueOf(gender);
    }
}
