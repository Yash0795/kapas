package com.kapas.vendor.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kapas.user.entity.User;
import com.kapas.util.AppUtils;
import com.kapas.util.SearchOperation;
import com.kapas.util.SearchOperationEnum;
import com.kapas.vendor.entity.IdType_;
import com.kapas.vendor.entity.Vendor;
import com.kapas.vendor.entity.VendorType_;
import com.kapas.vendor.entity.Vendor_;
import com.kapas.vendor.mapper.VendorMapper;
import com.kapas.vendor.model.PaginatedResponse;
import com.kapas.vendor.model.VendorRequest;
import com.kapas.vendor.model.VendorResponse;
import com.kapas.vendor.model.VendorSearch;
import com.kapas.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorService {

    private static final Logger LOGGER = LogManager.getLogger(VendorService.class);
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Value("classpath:/static/state-city.json")
    private Resource stateCityResource;

    private Vendor getVendorById(Integer vendorId) throws Exception {
        Optional<Vendor> optionalVendor = vendorRepository.findByIdAndFetchVendorTypeAndIdType(vendorId);
        return optionalVendor.orElseThrow(() -> new Exception("Vendor Not Found"));
    }

    private void validateVendorByNameAndMobile(String firstName, String lastName, String mobile) throws Exception {
        Optional<Vendor> optionalVendor = vendorRepository.findByNameAndMobile(firstName, lastName, mobile);
        if (optionalVendor.isPresent())
            throw new Exception("Vendor with first name, last name and mobile already exists");
    }

    private void validateVendorStateCity(String state, String city) throws Exception {
        Map<String, List<String>> stateCityMap = getStateAndCity();
        if (!stateCityMap.containsKey(state))
            throw new Exception("State not found");
        else {
            List<String> cityList = stateCityMap.get(state);
            if (!cityList.contains(city))
                throw new Exception("State-City combination not found");
        }
    }

    private Map<String, List<String>> getStateAndCity() {
        Map<String, List<String>> stateCityMap = new HashMap<>();
        try {
            File file = stateCityResource.getFile();
            String content = new String(Files.readAllBytes(file.toPath()));
            Gson gson = new Gson();
            Type stateCityMapType = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            stateCityMap = gson.fromJson(content, stateCityMapType);
        } catch (Exception e) {
            LOGGER.error("Error in Parsing state-city.json");
        }

        return stateCityMap;
    }

    @Transactional(rollbackFor = Throwable.class)
    public VendorResponse createVendor(VendorRequest vendorRequest, User user) throws Exception {
        validateVendorByNameAndMobile(vendorRequest.getFirstName(), vendorRequest.getLastName(), vendorRequest.getMobile());
        validateVendorStateCity(vendorRequest.getState(), vendorRequest.getCity());
        Vendor vendor = vendorMapper.vendorRequestToVendor(vendorRequest, user);
        vendor = vendorRepository.persist(vendor);
        return vendorMapper.vendorToVendorResponse(vendor);
    }

    @Transactional(readOnly = true)
    public VendorResponse getVendor(Integer vendorId) throws Exception {
        Vendor vendor = getVendorById(vendorId);
        return vendorMapper.vendorToVendorResponse(vendor);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<VendorResponse> getAllVendors(int pageNo, int pageSize, String sortBy, String sortDir,
                                                           VendorSearch vendorSearch) throws Exception {
        boolean validSortBy = AppUtils.doesObjectContainField(Vendor_.class, sortBy);
        if(!validSortBy) {
            throw new Exception("Sort by is not valid");
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Vendor> specification = (vendor, query, cb) -> {
            if(isCountQuery(query)) {
                vendor.join(Vendor_.VENDOR_TYPE, JoinType.INNER);
                vendor.join(Vendor_.ID_TYPE, JoinType.INNER);
            } else {
                vendor.fetch(Vendor_.VENDOR_TYPE, JoinType.INNER);
                vendor.fetch(Vendor_.ID_TYPE, JoinType.INNER);
            }
            List<Predicate> searchList = new ArrayList<>();
            if(Objects.nonNull(vendorSearch)) {
                List<SearchOperation<String>> searchOperationList = new ArrayList<>();
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS, vendor.get(Vendor_.FIRST_NAME),
                        vendorSearch.getName()));
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS, vendor.get(Vendor_.LAST_NAME),
                        vendorSearch.getName()));
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS, vendor.get(Vendor_.MOBILE),
                        vendorSearch.getMobile()));
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS,
                        vendor.get(Vendor_.VENDOR_TYPE).get(VendorType_.TYPE), vendorSearch.getVendorType()));
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS,
                        vendor.get(Vendor_.ID_TYPE).get(IdType_.TYPE), vendorSearch.getIdType()));
                searchOperationList.add(new SearchOperation<>(SearchOperationEnum.CONTAINS,
                        vendor.get(Vendor_.ID_NUMBER), vendorSearch.getIdNumber()));

                searchList = AppUtils.getPredicateList(searchOperationList, cb);
            }
            if (searchList.isEmpty()) {
                searchList.add(cb.conjunction());
            }
            return cb.or(searchList.toArray(new Predicate[0]));
        };

        Page<Vendor> vendors = vendorRepository.findAll(specification, pageable);
        return vendorMapper.vendorToVendorResponse(vendors);
    }

    private boolean isCountQuery(CriteriaQuery<?> query) {
        return query.getResultType() == long.class || query.getResultType() == Long.class;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteVendor(Integer vendorId) throws Exception {
        Vendor vendor = getVendorById(vendorId);
        vendorRepository.delete(vendor);
    }

    @Transactional(rollbackFor = Throwable.class)
    public VendorResponse updateVendor(VendorRequest vendorRequest, User user, Integer vendorId) throws Exception {
        validateVendorByNameAndMobile(vendorRequest.getFirstName(), vendorRequest.getLastName(), vendorRequest.getMobile());
        validateVendorStateCity(vendorRequest.getState(), vendorRequest.getCity());
        Vendor vendor = getVendorById(vendorId);
        vendorMapper.updatedVendorRequestToVendor(vendorRequest, user, vendor);
        vendor = vendorRepository.merge(vendor);
        return vendorMapper.vendorToVendorResponse(vendor);
    }
}
