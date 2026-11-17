package bj.lapins.suivi.controller;

import bj.lapins.suivi.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/", "/dashboard"})
    public String afficherDashboard(Model model) {
        model.addAttribute("indicateurs", dashboardService.calculerIndicateursDuMois());
        return "dashboard";
    }
}
