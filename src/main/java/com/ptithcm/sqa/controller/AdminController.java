package com.ptithcm.sqa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.service.NguoiDungService;
import com.ptithcm.sqa.service.ThuocService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ThuocService thuocService;
    
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("dashboard")
    public String getDashBoard() {
        return "admin/dashboard";
    }
    
    // =========== QUẢN LÝ THUỐC =============
    
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
    
    // =========== QUẢN LÝ NHÂN VIÊN =============
    
    // Hiển thị danh sách nhân viên
    @GetMapping("/employees")
    public String showEmployees(Model model, 
                               @RequestParam(defaultValue = "1") int page,
                               HttpSession session) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("LoggedNguoiDung") == null) {
            return "redirect:/login";
        }
        
        // Kiểm tra vai trò
        NguoiDung loggedUser = (NguoiDung) session.getAttribute("LoggedNguoiDung");
        if (loggedUser.getVaiTro() != NguoiDung.UserRole.ql) {
            return "redirect:/";
        }
        
        // Phân trang
        Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("maNguoiDung").descending());
        Page<NguoiDung> employeesPage = nguoiDungService.getAllEmployees(pageable);
        
        model.addAttribute("employees", employeesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        
        return "admin/employees";
    }

    // Thêm nhân viên mới
    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute NguoiDung nguoiDung,
                             RedirectAttributes redirectAttributes) {
        
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (nguoiDungService.isPhoneNumberExists(nguoiDung.getSoDienThoai())) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Số điện thoại đã được sử dụng bởi người dùng khác");
            return "redirect:/admin/employees";
        }
        
        // Lưu nhân viên mới
        nguoiDungService.saveEmployee(nguoiDung);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên mới thành công");
        
        return "redirect:/admin/employees";
    }

    // Sửa thông tin nhân viên
    @PostMapping("/employees/edit")
    public String updateEmployee(@ModelAttribute NguoiDung nguoiDungUpdate,
                               RedirectAttributes redirectAttributes) {
        
        // Kiểm tra người dùng tồn tại không
        Optional<NguoiDung> nguoiDungOpt = nguoiDungService.getEmployeeById(nguoiDungUpdate.getMaNguoiDung());
        if (!nguoiDungOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên");
            return "redirect:/admin/employees";
        }
        
        NguoiDung nguoiDung = nguoiDungOpt.get();
        
        // Kiểm tra số điện thoại đã tồn tại ở người dùng khác chưa
        if (nguoiDungService.isPhoneNumberExistsExcept(nguoiDungUpdate.getSoDienThoai(), nguoiDung.getMaNguoiDung())) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Số điện thoại đã được sử dụng bởi người dùng khác");
            return "redirect:/admin/employees";
        }
        
        // Cập nhật thông tin
        nguoiDung.setTenNguoiDung(nguoiDungUpdate.getTenNguoiDung());
        nguoiDung.setSoDienThoai(nguoiDungUpdate.getSoDienThoai());
        nguoiDung.setDiaChi(nguoiDungUpdate.getDiaChi());
        nguoiDung.setVaiTro(nguoiDungUpdate.getVaiTro());
        
        // Chỉ cập nhật mật khẩu nếu có nhập mật khẩu mới
        if (nguoiDungUpdate.getMatKhau() != null && !nguoiDungUpdate.getMatKhau().isEmpty()) {
            nguoiDung.setMatKhau(nguoiDungUpdate.getMatKhau());
        }
        
        // Lưu thay đổi
        nguoiDungService.saveEmployee(nguoiDung);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin nhân viên thành công");
        
        return "redirect:/admin/employees";
    }

    // Xóa nhân viên
    @PostMapping("/employees/delete")
    public String deleteEmployee(@RequestParam Long maNguoiDung,
                               RedirectAttributes redirectAttributes) {
        
        // Kiểm tra người dùng tồn tại không
        Optional<NguoiDung> nguoiDungOpt = nguoiDungService.getEmployeeById(maNguoiDung);
        if (!nguoiDungOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên");
            return "redirect:/admin/employees";
        }
        
        // Xóa nhân viên
        nguoiDungService.deleteEmployee(maNguoiDung);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa nhân viên thành công");
        
        return "redirect:/admin/employees";
    }
}
