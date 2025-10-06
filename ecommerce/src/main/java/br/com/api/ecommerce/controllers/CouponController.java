package br.com.api.ecommerce.controllers;

import br.com.api.ecommerce.models.Coupon;
import br.com.api.ecommerce.models.dtos.Coupon.CouponDtoCreate;
import br.com.api.ecommerce.models.dtos.Coupon.CouponDtoUpdate;
import br.com.api.ecommerce.services.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService service;

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody @Valid CouponDtoCreate dto){
        Coupon coupon = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }

    @GetMapping
    public ResponseEntity<Page<Coupon>> getAll(Pageable pageable){
        Page<Coupon> coupons = service.getAll(pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getById(@PathVariable UUID id){
        Coupon coupon = service.getById(id);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping
    public ResponseEntity<Coupon> update(@RequestBody @Valid CouponDtoUpdate dto){
        Coupon coupon = service.update(dto);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
