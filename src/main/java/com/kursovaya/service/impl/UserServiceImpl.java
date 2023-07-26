package com.kursovaya.service.impl;

import com.kursovaya.constant.UserImplConstant;
import com.kursovaya.enumeration.Role;
import com.kursovaya.exception.domain.*;
import com.kursovaya.repository.UserRepository;
import com.kursovaya.service.LoginAttemptService;
import com.kursovaya.service.UserService;
import com.kursovaya.entity.User;
import com.kursovaya.entity.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;


@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            LOGGER.error(UserImplConstant.NO_USER_FOUND_BY_EMAIL + email);
            throw new UsernameNotFoundException(UserImplConstant.NO_USER_FOUND_BY_EMAIL + email);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String email, String phoneNumber, String password) throws UserNotFoundException, EmailExistException, EmailNotValidException, PasswordNotValidException, PhoneNumberNotValidException {
        validateNewEmail(EMPTY, email);
        validatePassword(password);
        validatePhoneNumber(phoneNumber);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        userRepository.save(user);
//        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }


    @Override
    public User updateUser(String currentEmail, String newFirstName, String newLastName, String newEmail, String phoneNumber, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, EmailExistException, EmailNotValidException,  PhoneNumberNotValidException {
        User currentUser = validateNewEmail(currentEmail, newEmail);
        validatePhoneNumber(phoneNumber);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public User updatePassword(String currentEmail, String currentPassword, String newPassword) throws PasswordNotValidException {
        this.validatePassword(newPassword);
        User currentUser = userRepository.findUserByEmail(currentEmail);
        if (passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            currentUser.setPassword(encodePassword(newPassword));
        }
        return currentUser;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByID(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findUserByEmail(email);
        userRepository.deleteById(user.getId());
    }


    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getEmail())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }

    private void validatePhoneNumber(String phoneNumber) throws PhoneNumberNotValidException {
        Pattern pattern = Pattern.compile("^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$");
        if (!phoneNumber.matches(pattern.pattern())){
            throw new PhoneNumberNotValidException("Телефон не действителен");
        }
    }


    private void validatePassword(String password) throws PasswordNotValidException {
        if (!(password.length() >= 6)) {
            throw new PasswordNotValidException("Пароль не действителен");
        }
    }

    private User validateNewEmail(String currentEmail, String newEmail) throws UserNotFoundException, EmailExistException, EmailNotValidException {
        if (!EmailValidator.getInstance().isValid(newEmail)) {
            throw new EmailNotValidException("Почта не действительна: " + newEmail);
        }
        User userByNewEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentEmail)) {
            User currentUser = findUserByEmail(currentEmail);
            if (currentUser == null) {
                throw new UserNotFoundException(UserImplConstant.NO_USER_FOUND_BY_EMAIL + currentEmail);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewEmail != null) {
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
}
