package com.ptithcm.sqa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Thuoc> findProducts(String group, String search) {
        List<Thuoc> thuocList;

        if (search == null || search.isEmpty()) {
            thuocList = thuocRepository.findAll();
        } else {
            thuocList = thuocRepository.searchProducts(group, search);
        }

        if (group == null || group.isEmpty()) {
            return thuocList;
        }

        return thuocList.stream()
                .filter(t -> {
                    String ageUse = t.getTuoiSuDung();
                    switch (group) {
                        case "over18":

                            return ageUse.equals("Trên 18 tuổi");
                        case "over12":

                            return ageUse.equals("Trên 12 tuổi");
                        case "over8":

                            return ageUse.equals("Trên 8 tuổi");
                        case "over4":

                            return ageUse.equals("Trên 4 tuổi");
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

    }
}
