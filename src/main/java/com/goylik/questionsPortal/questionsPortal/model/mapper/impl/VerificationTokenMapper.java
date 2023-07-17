package com.goylik.questionsPortal.questionsPortal.model.mapper.impl;

import com.goylik.questionsPortal.questionsPortal.model.dto.token.VerificationTokenDto;
import com.goylik.questionsPortal.questionsPortal.model.entity.token.VerificationToken;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IUserMapper;
import com.goylik.questionsPortal.questionsPortal.model.mapper.IVerificationTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenMapper implements IVerificationTokenMapper {
    private IUserMapper userMapper;
    @Override
    public VerificationTokenDto map(VerificationToken entity) {
        VerificationTokenDto tokenDto = null;
        if (entity != null) {
            tokenDto = new VerificationTokenDto();
            tokenDto.setId(entity.getId());
            tokenDto.setToken(entity.getToken());
            tokenDto.setExpiryDate(entity.getExpiryDate());
            tokenDto.setUser(this.userMapper.map(entity.getUser()));
        }

        return tokenDto;
    }

    @Override
    public VerificationToken map(VerificationTokenDto dto) {
        VerificationToken token = null;
        if (dto != null) {
            token = new VerificationToken();
            token.setId(dto.getId());
            token.setToken(dto.getToken());
            token.setExpiryDate(dto.getExpiryDate());
            token.setUser(this.userMapper.map(dto.getUser()));
        }

        return token;
    }

    @Autowired
    public void setUserMapper(IUserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
