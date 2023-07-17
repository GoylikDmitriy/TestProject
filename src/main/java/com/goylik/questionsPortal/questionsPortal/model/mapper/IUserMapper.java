package com.goylik.questionsPortal.questionsPortal.model.mapper;

import com.goylik.questionsPortal.questionsPortal.model.dto.UserDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.User;

public interface IUserMapper extends ModelMapper<User, UserDto> {
    User mapToRegistration(UserDto dto);

    UserDto mapToEdit(UserDto dto);
}
