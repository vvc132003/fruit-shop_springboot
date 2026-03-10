package com.fruitshop.fruit_shop.controllerAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.fruit_shop.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final DashboardService dashboardService;

    public DashboardApiController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/chartData")
    public Object chartData(){
        return dashboardService.getRevenueChart();
    }

    @GetMapping("/orderChartData")
    public Object orderChartData(){
        return dashboardService.getOrderChart();
    }
}