package com.kapas.vendor.controller;

import com.kapas.user.annotation.PermissionScopeValidation;
import com.kapas.user.entity.User;
import com.kapas.user.model.PermissionEnum;
import com.kapas.user.model.ScopeEnum;
import com.kapas.util.Constants;
import com.kapas.vendor.entity.Vendor_;
import com.kapas.vendor.model.PaginatedResponse;
import com.kapas.vendor.model.VendorRequest;
import com.kapas.vendor.model.VendorResponse;
import com.kapas.vendor.model.VendorSearch;
import com.kapas.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    @PermissionScopeValidation(scope = ScopeEnum.VENDOR, permission = PermissionEnum.ADD)
    public ResponseEntity<VendorResponse> createVendor(@Valid @RequestBody VendorRequest vendorRequest,
                                                       HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute(Constants.PRINCIPAL);
        VendorResponse vendorResponse = vendorService.createVendor(vendorRequest, user);
        return new ResponseEntity<>(vendorResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{vendorId}")
    @PermissionScopeValidation(scope = ScopeEnum.VENDOR, permission = PermissionEnum.VIEW)
    public ResponseEntity<VendorResponse> getVendor(@PathVariable("vendorId") Integer vendorId) throws Exception {
        VendorResponse vendorResponse = vendorService.getVendor(vendorId);
        return new ResponseEntity<>(vendorResponse, HttpStatus.OK);
    }

    @PostMapping("/search")
    @PermissionScopeValidation(scope = ScopeEnum.VENDOR, permission = PermissionEnum.VIEW)
    public ResponseEntity<PaginatedResponse<VendorResponse>> getAllVendors(
            @RequestParam(value = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = Vendor_.ID, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
            @RequestBody VendorSearch vendorSearch
            ) throws Exception {
        PaginatedResponse<VendorResponse> vendorResponse = vendorService.getAllVendors(pageNo, pageSize, sortBy, sortDir, vendorSearch);
        return new ResponseEntity<>(vendorResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{vendorId}")
    @PermissionScopeValidation(scope = ScopeEnum.VENDOR, permission = PermissionEnum.DELETE)
    public ResponseEntity<String> deleteVendor(@PathVariable("vendorId") Integer vendorId) throws Exception {
        vendorService.deleteVendor(vendorId);
        return new ResponseEntity<>("Vendor Deleted Successfully", HttpStatus.OK);
    }

    @PutMapping("/{vendorId}")
    @PermissionScopeValidation(scope = ScopeEnum.VENDOR, permission = PermissionEnum.UPDATE)
    public ResponseEntity<VendorResponse> updateVendor(@Valid @RequestBody VendorRequest vendorRequest,
                                                       HttpServletRequest request,
                                                       @PathVariable("vendorId") Integer vendorId) throws Exception {
        User user = (User) request.getAttribute(Constants.PRINCIPAL);
        VendorResponse vendorResponse = vendorService.updateVendor(vendorRequest, user, vendorId);
        return new ResponseEntity<>(vendorResponse, HttpStatus.OK);
    }
}
