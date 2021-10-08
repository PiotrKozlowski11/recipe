package org.recipes.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Recipe class, stored with use of JPA.
 */
@Entity
@Table(name = "recipe")
//@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence"
    )
    @JsonIgnore
    private int id;


    /**
     * Name of a recipe
     */
    @NotBlank
    private String name;

    /**
     * Category of a recipe
     */
    @NotBlank
    private String category;

    /**
     * Last time when recipe was created or updated (whichever is later)
     */
    @UpdateTimestamp
    private LocalDateTime date;

    /**
     * Description of a recipe
     */
    @NotBlank
    private String description;


    /**
     * List of ingredients used to make a recipe, stored in another table. Minimum ingredients=1
     */
    @NotNull
    @Size(min = 1)
    @ElementCollection
    private List<String> ingredients;

    /**
     * List of directions used to make a recipe, stored in another table. Minimum directions=1
     */
    @NotNull
    @Size(min = 1)
    @ElementCollection
    private List<String> directions;

    /**
     * User which is owner of a given recipe, each recipe has only one owner
     */
    @ManyToOne
    @JoinColumn(name = "Userid")
    @JsonIgnore
    private User user;

    /**
     * Required args constructor
     *
     * @param name        name of a recipe
     * @param category    category of a recipe
     * @param description description of a recipe
     * @param ingredients list of ingredients used
     * @param directions  list of directions used
     * @param user        owner of a recipe
     */
    public Recipe(String name, String category, String description, List<String> ingredients, List<String> directions, User user) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }
}