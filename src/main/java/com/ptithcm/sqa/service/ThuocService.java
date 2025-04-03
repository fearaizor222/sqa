package com.ptithcm.sqa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.repository.ThuocRepository;

@Service
public class ThuocService {

    @Autowired
    private ThuocRepository thuocRepository;

    public List<Thuoc> getAllThuoc() {
        return thuocRepository.findAll();
    }

    public Optional<Thuoc> getThuocById(Integer id) {
        return thuocRepository.findById(id);
    }

    public Thuoc saveThuoc(Thuoc thuoc) {
        return thuocRepository.save(thuoc);
    }

    public void deleteThuoc(Integer id) {
        thuocRepository.deleteById(id);
    }
}
