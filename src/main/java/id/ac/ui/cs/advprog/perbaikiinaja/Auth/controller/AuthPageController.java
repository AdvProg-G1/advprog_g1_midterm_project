package id.ac.ui.cs.advprog.perbaikiinaja.Auth.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.RegisterUserRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.UserResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthStrategy auth;
    private final UserRepository userRepo;

    // ─── Views ───────────────────────────────────────────────────────────
    @GetMapping("/")
    public String root() {
        return "forward:/auth/welcome.html";
    }

    @GetMapping("/auth/register")
    public String showRegister() {
        return "forward:/auth/register.html";
    }

    @GetMapping("/auth/login")
    public String showLogin() {
        return "forward:/auth/login.html";
    }

    @GetMapping("/auth/welcome")
    public String showWelcome() {
        return "forward:/auth/welcome.html";
    }

    // ─── Register ────────────────────────────────────────────────────────
    @PostMapping("/auth/register")
    public String register(@ModelAttribute @Valid RegisterUserRequest req,
                           BindingResult br,
                           RedirectAttributes flash) {
        if (br.hasErrors()) {
            flash.addFlashAttribute("errors", br.getAllErrors());
            return "redirect:/auth/register";
        }
        auth.register(req);
        flash.addFlashAttribute("message", "Registration successful – please log in.");
        return "redirect:/auth/login";
    }

    // ─── Login ───────────────────────────────────────────────────────────
    @PostMapping("/auth/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        RedirectAttributes flash) {
        try {
            var user = auth.login(username, password);

            // Put user into the SecurityContext
            var authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Persist the context in the HTTP session
            request.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            // Role-based landing page
            String target = switch (user.getRoleEnum()) {
                case USER       -> "/user/home.html";
                case TECHNICIAN -> "/technician/home.html";
                case ADMIN      -> "/admin/home.html";
            };
            return "redirect:" + target;

        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/auth/login";
        }
    }

    // ─── REST for current user ───────────────────────────────────────────
    @GetMapping("/api/auth/current")
    @ResponseBody
    public UserResponse current(@AuthenticationPrincipal User ud) {
        if (ud == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new UserResponse(
                ud.getId(),
                ud.getUsername(),
                ud.getFullName(),
                ud.getEmail(),
                ud.getPhone(),
                ud.getAddress(),
                ud.getRole()
        );
    }

    // ─── REST to fetch any user by ID ───────────────────────────────────
    @GetMapping("/api/users/{id}")
    @ResponseBody
    public UserResponse getUserById(@PathVariable String id) {
        return userRepo.findById(id)
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getAddress(),
                        u.getRole()
                ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // ─── API to get all technicians for dropdown ─────────────────────────
    @GetMapping("/api/technicians")
    @ResponseBody
    public List<UserResponse> getAllTechnicians() {
        return auth.getAllTechnicians();
    }
}
