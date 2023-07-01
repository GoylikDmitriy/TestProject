package com.goylik.questionsPortal.questionsPortal.model.mapper;

import com.goylik.questionsPortal.questionsPortal.model.entity.BaseEntity;

public interface ModelMapper<E extends BaseEntity, O> {
    O map(E entity);
    E map(O dto);
}