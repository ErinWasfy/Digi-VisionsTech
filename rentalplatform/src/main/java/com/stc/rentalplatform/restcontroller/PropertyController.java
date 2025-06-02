package com.stc.rentalplatform.restcontroller;

import com.stc.rentalplatform.dto.PropertyDTO;
import com.stc.rentalplatform.entity.Property;
import com.stc.rentalplatform.mapper.PropertyMapper;
import com.stc.rentalplatform.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyMapper propertyMapper;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyDTO dto) {
        Property property = propertyMapper.toEntity(dto);
        // Set tenantId inside service if you want, or here with TenantContext.getTenant()
        Property saved = propertyService.save(property);
        return new ResponseEntity<>(propertyMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<PropertyDTO> getProperties(Pageable pageable) {
        return propertyService.getProperties(pageable)
                .map(propertyMapper::toDto);
    }
    @GetMapping("/search")
    public List<Property> searchProperties(
            @RequestParam String location,
            @RequestParam(required = false) List<String> amenities
    ) {
        return propertyService.searchProperties(location, amenities);
    }
    @PutMapping("/{id}")
    public Property update(@PathVariable Long id, @RequestBody Property property) {
        return propertyService.update(id, property);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        propertyService.delete(id);
    }
}
