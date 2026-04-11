package skytrack.business.mapper;

import skytrack.dto.user.RegisterUserRequest;
import skytrack.dto.user.UpdateUserRequest;
import skytrack.dto.user.UserResponse;
import skytrack.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.time.Period;

public class UserMapper {
    public static UserResponse toResponse(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .picture(user.getPicture())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(Period.between(user.getBirthdate(), LocalDate.now()).getYears())
                .role(user.getRole().getRoleName().name())
                .build();
    }

    public static UserEntity toEntity(RegisterUserRequest request){
        return UserEntity.builder()
                .picture(request.getPicture())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .birthdate(request.getBirthDate())
                .build();
    }
}
