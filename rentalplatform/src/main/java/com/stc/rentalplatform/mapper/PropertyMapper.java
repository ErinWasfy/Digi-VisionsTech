package com.stc.rentalplatform.mapper;


import com.stc.rentalplatform.dto.PropertyDTO;
import com.stc.rentalplatform.entity.Property;
import  org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    PropertyDTO toDto(Property property);
    Property toEntity(PropertyDTO dto);
}
