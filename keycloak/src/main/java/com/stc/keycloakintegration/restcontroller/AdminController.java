package com.stc.keycloakintegration.restcontroller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')") // same applies to users/owners
public class AdminController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Admin Dashboard";
    }
}
