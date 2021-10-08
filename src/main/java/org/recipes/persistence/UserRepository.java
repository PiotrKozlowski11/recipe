package org.recipes.persistence;


import org.recipes.business.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository which is used to send queries to database.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Find user by his name
     *
     * @param userName - name of a user
     * @return Optional user
     */
    Optional<User> findByUserName(String userName);
}
