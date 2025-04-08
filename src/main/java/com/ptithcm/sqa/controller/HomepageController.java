package com.ptithcm.sqa.controller;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.service.ThuocService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ptithcm.sqa.repository.ThuocRepository;

import java.util.List;

@Controller
public class HomepageController {

    private static final int PRODUCTS_PER_PAGE = 12;

    @Autowired
    private ThuocService thuocService;

    @Autowired
    private ThuocRepository ThuocRepository;

    private String mapGroupToAge(String group) {
        switch (group) {
            case "over18":
                return "Trên 18 tuổi";
            case "over12":
                return "Trên 12 tuổi";
            case "over8":
                return "Trên 8 tuổi";
            case "over4":
                return "Trên 4 tuổi";
            case "all":
            case "":
                return "all";
            default:
                return "all";
        }
    }

    @GetMapping("/")
    public String homepage(
            @RequestParam(name = "group", required = false, defaultValue = "") String group,
            @RequestParam(name = "type", required = false, defaultValue = "") String type,
            @RequestParam(name = "category", required = false, defaultValue = "") String category,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model, HttpSession session) {

        String mappedGroup = mapGroupToAge(group);
        List<Thuoc> allProducts = thuocService.findProducts(mappedGroup, search);

        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);

        int start = (page - 1) * PRODUCTS_PER_PAGE;
        int end = Math.min(start + PRODUCTS_PER_PAGE, totalProducts);
        List<Thuoc> products = allProducts.subList(start, end);

        boolean hasProducts = !products.isEmpty();

        model.addAttribute("products", products);
        model.addAttribute("total_pages", totalPages);
        model.addAttribute("page", page);
        model.addAttribute("group", group);
        model.addAttribute("type", type);
        model.addAttribute("category", category);
        model.addAttribute("search", search);
        model.addAttribute("has_products", hasProducts);

        Object loggedUser = session.getAttribute("LoggedNguoiDung");
        model.addAttribute("LoggedNguoiDung", loggedUser);

        return "user/homepage";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "group", required = false, defaultValue = "all") String group,
            Model model) {

        String mappedGroup = mapGroupToAge(group);
        List<Thuoc> products = ThuocRepository.searchProducts(mappedGroup, search);
        boolean hasProducts = !products.isEmpty();

        model.addAttribute("products", products);
        model.addAttribute("has_products", hasProducts);
        model.addAttribute("group", group);
        model.addAttribute("search", search);

        return "product-list";
    }
}
