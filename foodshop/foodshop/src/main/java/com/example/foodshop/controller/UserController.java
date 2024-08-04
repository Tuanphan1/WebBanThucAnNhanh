package com.example.foodshop.controller;

import com.example.foodshop.Entity.Role;
import com.example.foodshop.dto.LoginDto;
import com.example.foodshop.dto.SignUpDto;
import com.example.foodshop.Entity.User;
import com.example.foodshop.repository.RoleRepository;
import com.example.foodshop.repository.UserRepository;
import com.example.foodshop.response.ResponseObject;
import com.example.foodshop.response.UserResponse;
import com.example.foodshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllUsers() {
        try {
            var listUser = userService.findAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Get the list of users successfully",true, listUser)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred" + e.getMessage(), false, "")
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody SignUpDto signUpDto){
        try{
            // add check for username exists in a DB
            if(userRepository.existsByUsername(signUpDto.getUsername())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(404, "Account already exists",false, "")
                );
            }
            // add check for email exists in DB
            if(userRepository.existsByEmail(signUpDto.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject(404, "Email already exists",false, "")
                );
            }

            // create user object
            User user = new User();
            user.setUsername(signUpDto.getUsername());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            Role roles = roleRepository.findByName("ROLE_USER").get();
            user.setRoles(Collections.singleton(roles));

            var saveUser = userRepository.save(user);

            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(saveUser.getUsername());
            userResponse.setEmail(saveUser.getEmail());
            userResponse.setRoles(saveUser.getRoles());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Account registration successful",true, saveUser)
            );
        }catch (AuthenticationException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred", false, "")
            );
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseObject> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            // Retrieve user details from the database using the repository
            Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setEmail(user.getEmail());
                userResponse.setUsername(user.getUsername());
                userResponse.setRoles(user.getRoles());
                // Process user details as needed
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(200, "Logged in successfully", true, userResponse)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(404, "Login failed", false, "")
                );
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(500, "An error occurred", false, "")
            );
        }
    }
}


