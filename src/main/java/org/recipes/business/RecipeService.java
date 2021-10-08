package org.recipes.business;


import org.recipes.persistence.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Find recipe by a given id
     *
     * @param id of a recipe
     * @return found Recipe by id
     */
    public Recipe findRecipeById(int id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);


        return recipeOptional.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    /**
     * Save given recipe to specified user
     *
     * @param recipe to be saved
     * @param user   - owner of a recipe
     * @return Recipe with all information including assigned id and timestamp
     */
    public Recipe save(Recipe recipe, User user) {
        Recipe recipeSave = new Recipe(
                recipe.getName(),
                recipe.getCategory(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections(),
                user);
        return recipeRepository.save(recipeSave);
    }

    /**
     * Delete recipe by id
     *
     * @param id of recipe to be deleted
     */
    public void deleteById(int id) {

        boolean recipeExists = recipeRepository.existsById(id);
        if (!recipeExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        recipeRepository.deleteById(id);
    }

    /**
     * Update recipe with given id with specified recipe
     *
     * @param id     of updated recipe
     * @param recipe with updated information
     */
    public void updateRecipeById(int id, Recipe recipe) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipeOld = recipeOptional.get();

            recipeOld.setName(recipe.getName());
            recipeOld.setCategory(recipe.getCategory());
            recipeOld.setDescription(recipe.getDescription());
            recipeOld.setIngredients(recipe.getIngredients());
            recipeOld.setDirections(recipe.getDirections());

            recipeRepository.save(recipeOld);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Method used to find list of recipes with specified category and owned by a given user
     *
     * @param category name of a category
     * @param userName name of a user
     * @return List of found recipes
     */
    public List<Recipe> findByCategoryUser(String category, String userName) {
        return recipeRepository.findAllByCategoryIgnoreCaseAndUser_UserNameOrderByDateDesc(category, userName);
    }

    /**
     * Method used to find list of recipes containing given name and owned by a given user
     *
     * @param name     of a recipe
     * @param userName name of a user
     * @return List of found recipes
     */
    public List<Recipe> findByNameUser(String name, String userName) {
        return recipeRepository.findAllByNameContainingIgnoreCaseAndUser_UserNameOrderByDateDesc(name, userName);
    }
}
