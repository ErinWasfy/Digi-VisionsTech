package com.stc.rentalplatform.service;

import com.stc.rentalplatform.entity.Property;
import com.stc.rentalplatform.repository.PropertyRepository;
import com.stc.rentalplatform.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public Page<Property> getProperties(Pageable pageable) {
        return propertyRepository.findAllByTenant(TenantContext.getTenant(), pageable);
    }
    public Property save(Property property)
    {
      return   propertyRepository.save(property);
    }
    @Cacheable(
            value = "propertySearchCache",
            key = "T(java.util.Objects).hash(#location, #amenities)"
    )
    public List<Property> searchProperties(String location, List<String> amenities) {
        return propertyRepository.searchByCriteria(location, amenities);
    }
    public Property getById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }
    public Property update(Long id, Property updated) {
        Property p = getById(id);
        p.setName(updated.getName());
        p.setLocation(updated.getLocation());
        return propertyRepository.save(p);
    }
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

}
