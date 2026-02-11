package org.tibocv.apicv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tibocv.apicv.dto.PortfolioDTO;
import org.tibocv.apicv.service.PortfolioService;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public PortfolioDTO getPortfolio() {
        return portfolioService.getFullPortfolio();
    }
}