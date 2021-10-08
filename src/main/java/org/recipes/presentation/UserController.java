package org.recipes.presentation;


import org.recipes.business.User;
import org.recipes.business.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    /**
     * Autowired constructor
     *
     * @param userService - used to communicate with user database
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Post mapping. Used to register new user.
     *
     * @param user - with all valid fields:
     *             1. Email containing '@' and '.'.
     *             2. Password at least 8 characters long.
     * @return ResponseEntity with Http OK code
     * @throws ResponseStatusException if username already exists
     */
    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) throws ResponseStatusException {
        if (userService.userNameExists(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User userSave = new User(
                user.getUserName(),
                user.getPassword()
        );

        userService.save(userSave);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}


