package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/report/{orderId}")
public class RepairReportController {
    @Autowired
    private RepairReportServiceImpl service;

    public static class CreateReport {
        public Long technicianId;
        public String details;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createReportPost(Model model, @ModelAttribute RepairReport report) {
        String orderId       = report.getId();
        String technicianId = report.getTechnicianId();
        String details     = report.getDetails();

        RepairReport saved = service.createRepairReport(orderId, technicianId, details);
        return "redirect:/api/repair/list";
    }

    @GetMapping("/create")
    public String createReportPage(Model model) {
        RepairReport report = new RepairReport();
        model.addAttribute("reports", report);
        return "technical/create_report";    }
}
