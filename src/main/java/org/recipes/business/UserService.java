package org.recipes.business;


import org.recipes.business.security.MyUserDetails;
import org.recipes.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method used to load given user by name
     *
     * @param userName of user to be logged in
     * @return UserDetails of given user
     * @throws UsernameNotFoundException if user is not found in a database
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));

        return user.map(MyUserDetails::new).get();
    }

    /**
     * Used to find user by username
     *
     * @param userName - name of a user
     * @return instance of User class
     * @throws UsernameNotFoundException if user is not found in a database
     */
    public User findUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));
        return user.get();
    }


    /**
     * Checks whether user exists in a database
     *
     * @param userName - name of a user
     * @return true if user exists otherwise false
     */
    public boolean userNameExists(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    /**
     * Save user to a database
     *
     * @param user instance of User class
     */
    public void save(User user) {
        User userSave = new User(
                user.getUserName(),
                passwordEncoder.encode(user.getPassword())
        );
        userRepository.save(userSave);
    }


}