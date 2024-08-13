package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPartnerController {

    @GetMapping("/admin/partner")
    public String adminPartner() {
        return "views/admin/partner/partner";
    }

    @GetMapping("/admin/partner/placeManagement")
    public String placeManagement() {
        return "views/admin/partner/placeManagement";
    }
}