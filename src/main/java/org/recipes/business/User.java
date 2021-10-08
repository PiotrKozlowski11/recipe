package org.recipes.business;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents user. Stored with use of JPA.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {


    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer id;


    @JsonAlias("email")
    @Email
    @Pattern(regexp = ".*\\..*.*")
    private String userName;

    @Pattern(regexp = ".{8,}")
    @NotBlank
    private String password;

    /**
     * If user is active or not
     */
    private boolean isActive;

    /**
     * User roles. For example: ROLE_USER, ROLE_ADMIN
     */
    private String roles;

    /**
     * List of recipes which user is owning
     */
    @OneToMany(mappedBy = "user")
    private List<Recipe> recipeList = new ArrayList<>();

    /**
     * Constructor with required arg. By default, user is active and his role = 'ROLE_USER'
     *
     * @param userName name uf user
     * @param password his password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isActive = true;
        this.roles = "ROLE_USER";
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", roles='" + roles + '\'' +
                ", recipeList=" + recipeList +
                '}';
    }
}

