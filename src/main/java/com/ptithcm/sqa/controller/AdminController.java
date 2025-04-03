package com.ptithcm.sqa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.service.ThuocService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ThuocService thuocService;

    @GetMapping("dashboard")
    public String getDashBoard() {
        return "admin/dashboard";
    }
    @GetMapping("medicine")
    public String getMedicine(Model model) {
        List<Thuoc> thuocs = thuocService.getAllThuoc();
        model.addAttribute("thuocs", thuocs);
        return "admin/medicine";
    }
    
    @GetMapping("/medicines/add")
    public String showAddMedicineForm(Model model) {
        model.addAttribute("thuoc", new Thuoc());
        return "admin/add-medicine";
    }
    
    @PostMapping("/medicines/add")
    public String addMedicine(@ModelAttribute Thuoc thuoc, 
                             RedirectAttributes redirectAttributes) {
        try {
            thuocService.saveThuoc(thuoc);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm thuốc mới thành công!");
            return "redirect:/admin/medicine";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi thêm thuốc: " + e.getMessage());
            return "redirect:/admin/medicines/add";
        }
    }
    
    @GetMapping("/medicines/edit/{id}")
    public String showEditMedicineForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Thuoc> thuocOpt = thuocService.getThuocById(id);
        
        if (thuocOpt.isPresent()) {
            model.addAttribute("thuoc", thuocOpt.get());
            return "admin/edit-medicine";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thuốc với ID: " + id);
            return "redirect:/admin/medicine";
        }
    }
    
    @PostMapping("/medicines/edit/{id}")
    public String updateMedicine(@PathVariable("id") Integer id, @ModelAttribute Thuoc thuoc, 
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Thuoc> existingThuocOpt = thuocService.getThuocById(id);
            
            if (existingThuocOpt.isPresent()) {
                thuoc.setId(id); // Đảm bảo ID khớp với thuốc đang sửa
                thuocService.saveThuoc(thuoc);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thuốc thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thuốc với ID: " + id);
            }
            
            return "redirect:/admin/medicine";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật thuốc: " + e.getMessage());
            return "redirect:/admin/medicines/edit/" + id;
        }
    }
    
    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Thuoc> thuocOpt = thuocService.getThuocById(id);
            
            if (thuocOpt.isPresent()) {
                thuocService.deleteThuoc(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thuốc thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thuốc với ID: " + id);
            }
            
            return "redirect:/admin/medicine";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa thuốc: " + e.getMessage());
            return "redirect:/admin/medicine";
        }
    }
}
