package com.kursovaya.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.kursovaya.constant.SecurityConstant;
import com.kursovaya.entity.*;
import com.kursovaya.entity.dto.OrderDTO;
import com.kursovaya.entity.product.ProductType;
import com.kursovaya.exception.ExceptionHandling;
import com.kursovaya.exception.domain.*;
import com.kursovaya.service.UserService;
import com.kursovaya.service.OrderService;
import com.kursovaya.service.ProductService;
import com.kursovaya.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = { "/user"})
public class UserController extends ExceptionHandling {
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());
        User loginUser = userService.findUserByEmail(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, EmailNotValidException, PasswordNotValidException, NumberParseException, PhoneNumberNotValidException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getPassword());
        return new ResponseEntity<>(newUser, OK);
    }


    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestParam("currentEmail") String currentEmail,
                                       @RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName,
                                       @RequestParam("email") String email,
                                       @RequestParam("phoneNumber") String phoneNumber,
                                       @RequestParam("role") String role,
                                       @RequestParam("isActive") String isActive,
                                       @RequestParam("isNonLocked") String isNonLocked) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, EmailNotValidException, NumberParseException, PhoneNumberNotValidException {
        User updatedUser = userService.updateUser(currentEmail, firstName, lastName, email, phoneNumber, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(updatedUser, OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<User> updatePassword(@RequestParam("currentEmail") String currentEmail,
                                               @RequestParam("currentPassword") String currentPassword,
                                               @RequestParam("newPassword") String newPassword) throws UserNotFoundException, EmailExistException, PasswordNotValidException {
        User updatedUser = userService.updatePassword(currentEmail, currentPassword, newPassword);
        return new ResponseEntity<>(updatedUser, OK);
    }


    @GetMapping("/find/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        User user = userService.findUserByEmail(email);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
