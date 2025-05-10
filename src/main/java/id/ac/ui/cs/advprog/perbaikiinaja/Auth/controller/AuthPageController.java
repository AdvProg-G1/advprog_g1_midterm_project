package id.ac.ui.cs.advprog.perbaikiinaja.Auth.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthStrategy authStrategy;

    /** Root of the site → show welcome */
    @GetMapping("/")
    public String root() {
        return "forward:/auth/welcome.html";
    }

    /** Show the registration form */
    @GetMapping("/auth/register")
    public String showRegisterPage() {
        return "forward:/auth/register.html";
    }

    /** Handle form POST for registration */
    @PostMapping("/auth/register")
    public String register(
            @ModelAttribute @Valid RegisterUserRequest req,
            BindingResult binding,
            RedirectAttributes rttrs
    ) {
        if (binding.hasErrors()) {
            rttrs.addFlashAttribute("errors", binding.getAllErrors());
            return "redirect:/auth/register";
        }
        authStrategy.register(req);
        rttrs.addFlashAttribute("message", "Registration successful – please log in.");
        return "redirect:/auth/login";
    }

    /** Show the login form */
    @GetMapping("/auth/login")
    public String showLoginPage() {
        return "forward:/auth/login.html";
    }

    /** Handle form POST for login */
    @PostMapping("/auth/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest req,
            RedirectAttributes rttrs
    ) {
        try {
            var user = authStrategy.login(email, password);
            req.getSession().setAttribute("currentUser", user);
            // redirect to the mapped welcome endpoint
            return "redirect:/auth/welcome";
        } catch (RuntimeException ex) {
            rttrs.addFlashAttribute("error", ex.getMessage());
            return "redirect:/auth/login";
        }
    }

    /** Show the welcome page */
    @GetMapping("/auth/welcome")
    public String showWelcome() {
        return "forward:/auth/welcome.html";
    }
}
