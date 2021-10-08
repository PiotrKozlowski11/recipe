package org.recipes.persistence;


import org.recipes.business.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Recipe repository which is used to send queries to database.
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    //List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    //List<Recipe> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);

    /**
     * Find all Recipes by given category and given userName. Ignores case of letters
     * ordered by date descending (from newest).
     *
     * @param category - name of a category to be found
     * @param userName - name of a user who is owner of recipe
     * @return List of recipes
     */
    List<Recipe> findAllByCategoryIgnoreCaseAndUser_UserNameOrderByDateDesc(String category, String userName);

    /**
     * Find all Recipes by containing given name and given userName. Ignores case of letters
     * ordered by date descending (from newest).
     *
     * @param name     - name to be found
     * @param userName - name of a user who is owner of recipe
     * @return List of recipes
     */
    List<Recipe> findAllByNameContainingIgnoreCaseAndUser_UserNameOrderByDateDesc(String name, String userName);
}

