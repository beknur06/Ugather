package ugather.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ugather.dto.AppUserCreatDto;
import ugather.dto.Login;
import ugather.model.AppUser;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import ugather.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signin")
    @Operation(summary = "User Signin", description = "User login with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "422", description = "Invalid email/password supplied")})
    public String login(@RequestBody Login login) {
        return userService.signin(login.getEmail(), login.getPassword());
    }

    @PostMapping("/signup")
    @Operation(summary = "User Signup", description = "User registration with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "422", description = "Email is already in use")})
    public String signup(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Signup User", required = true, content = @Content(schema = @Schema(implementation = AppUserCreatDto.class))) @RequestBody AppUserCreatDto user) {
        return userService.signup(modelMapper.map(user, AppUser.class));
    }

    @GetMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Refresh user token")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }
}