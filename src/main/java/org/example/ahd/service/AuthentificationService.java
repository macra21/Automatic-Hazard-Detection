package org.example.ahd.service;

import org.example.ahd.domain.User;
import org.example.ahd.repository.UserRepository;
import org.example.ahd.utils.Encryption;
import org.example.ahd.validators.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthentificationService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public AuthentificationService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Transactional
    public User login(String email, String password) {
        return userRepository.findByMailAndPassword(email, password);
    }

    @Transactional
    public void register(User user){
        userValidator.validate(user);
        String hashedPassword = Encryption.SHA256OneWayHash(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.add(user);
    }
}
