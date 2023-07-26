package com.kursovaya.service;

import com.kursovaya.exception.domain.*;
import com.kursovaya.entity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String email, String phoneNumber, String password) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, EmailNotValidException, PasswordNotValidException, PhoneNumberNotValidException;

    List<User> getUsers();

    User findUserByEmail(String email);

    User findUserByID(Long id);

    User updateUser(String currentEmail, String newFirstName, String newLastName, String newEmail, String phoneNumber, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, EmailNotValidException, PhoneNumberNotValidException;
    User updatePassword(String currentEmail, String currentPassword, String newPassword) throws UserNotFoundException, EmailExistException, PasswordNotValidException;

    void deleteUser(String email) throws IOException;

}
