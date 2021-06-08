package app.preciojusto.application.dto.mappers;

import app.preciojusto.application.dto.UserResponseDTO;
import app.preciojusto.application.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    UserResponseDTO userToUserResponseDto(User user);
}
