package com.ptithcm.sqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.service.ThuocService;

import java.util.List;

@RestController
@RequestMapping("/api/thuoc")
public class ThuocController {

    @Autowired
    private ThuocService thuocService;

    @GetMapping
    public List<Thuoc> getAllThuoc() {
        return thuocService.getAllThuoc();
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Thuoc> getThuocById(@PathVariable Long id) {
    //     return thuocService.getThuocById(id)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }

    // @PostMapping
    // public Thuoc createThuoc(@RequestBody Thuoc thuoc) {
    //     return thuocService.saveThuoc(thuoc);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<Thuoc> updateThuoc(@PathVariable Long id, @RequestBody Thuoc thuoc) {
    //     return thuocService.getThuocById(id)
    //             .map(existingThuoc -> {
    //                 // Update fields
    //                 existingThuoc.setTenThuoc(thuoc.getTenThuoc());
    //                 existingThuoc.setSoLuong(thuoc.getSoLuong());
    //                 existingThuoc.setGia(thuoc.getGia());
    //                 return ResponseEntity.ok(thuocService.saveThuoc(existingThuoc));
    //             })
    //             .orElse(ResponseEntity.notFound().build());
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteThuoc(@PathVariable Long id) {
    //     if (thuocService.getThuocById(id).isPresent()) {
    //         thuocService.deleteThuoc(id);
    //         return ResponseEntity.noContent().build();
    //     }
    //     return ResponseEntity.notFound().build();
    // }
}
