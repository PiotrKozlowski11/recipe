package org.recipes.presentation;


import org.recipes.business.Recipe;
import org.recipes.business.RecipeService;
import org.recipes.business.User;
import org.recipes.business.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {


    private final RecipeService recipeService;

    private final UserService userDetailsService;

    /**
     * Autowired constructor
     *
     * @param recipeService      - used to communicate with recipe database
     * @param userDetailsService - used to communicate with user database
     */
    public RecipeController(RecipeService recipeService, UserService userDetailsService) {
        this.recipeService = recipeService;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Post mapping to post new recipe by user. User must be logged in to post new recipe
     *
     * @param recipe    - with all valid fields:
     *                  NotBlank name
     *                  NotBlank category
     *                  NotBlank description
     *                  List of ingredients (minimum of 1), mustn't be null
     *                  List of directions (minimum of 1), mustn't be null
     *                  Time will be generated automatically, user will be gotten from principal
     * @param principal - logged user
     * @return ResponseEntity with body of Map String 'id' and Integer id of generated recipe
     */
    @PostMapping("/api/recipe/new")
    public ResponseEntity<Map<String, Integer>> postRecipe(@Valid @RequestBody Recipe recipe, Principal principal) {

        User user = userDetailsService.findUserByUsername(principal.getName());

        Recipe recipeSave = recipeService.save(recipe, user);

        return ResponseEntity.ok(
                Map.of("id", recipeSave.getId()
                        //, "username: ", principal.getName()
                ));
    }


    /**
     * Get Mapping. Get recipe by id. Use must be logged in to obtain selected recipe.
     *
     * @param id        - of selected recipe
     * @param principal - logged user
     * @return Recipe selected by id
     * @throws ResponseStatusException with FORBIDDEN code when user is not owner of selected recipe
     */
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id,
                            Principal principal) throws ResponseStatusException {

        Recipe recipe = recipeService.findRecipeById(id);
        User user = userDetailsService.findUserByUsername(principal.getName());
        //System.out.println("User: " + user);
        //System.out.println("Recipe: " + recipe);
        if (recipe.getUser().equals(user)) {
            return recipe;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);


    }

    /**
     * Get mapping which searches recipe by:
     * 1. Category - Find all Recipes by given category and given userName.
     * Ignores case of letters ordered by date descending (from newest).
     * 2. Name - Find all Recipes by containing given name and given userName.
     * Ignores case of letters ordered by date descending (from newest).
     * If both parameters are given or both parameters are null ResponseStatusException is raised
     *
     * @param category  - category to be found
     * @param name      - name to be found
     * @param principal - logged user
     * @return ResponseEntity with list of found recipes
     * @throws ResponseStatusException with BAD_REQUEST code
     */
    @GetMapping("/api/recipe/search")
    public ResponseEntity<List<Recipe>> searchRecipe(@RequestParam(required = false) String category,
                                                     @RequestParam(required = false) String name,
                                                     Principal principal) throws ResponseStatusException {

        if ((category == null && name == null) || (category != null && name != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (category != null) {


            return ResponseEntity
                    .ok()
                    .body(recipeService.findByCategoryUser(category, principal.getName()));

        } else {

            return ResponseEntity
                    .ok()
                    .body(recipeService.findByNameUser(name, principal.getName()));


        }


    }

    /**
     * Delete mapping. Delete selected recipe by its id.
     * User must be logged in and be owner of recipe to perform action.
     *
     * @param id        - recipe's id
     * @param principal - logged user. Owner of selected recipe
     * @return ResponseEntity with NO_CONTENT code
     * @throws ResponseStatusException if user is not owner of selected recipe
     */
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable int id, Principal principal) throws ResponseStatusException {

        User user = userDetailsService.findUserByUsername(principal.getName());
        Recipe recipe = recipeService.findRecipeById(id);

        if (recipe.getUser().equals(user)) {
            recipeService.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    /**
     * Put mapping. Update recipe by its id. Updated recipe must follow same rules as when creating new recipe.
     * To update selected recipe user must be logged in and be owner of the recipe.
     *
     * @param id           of selected recipe
     * @param recipeUpdate - recipe which will replace old recipe. Must follow same rules as when creating new recipe.
     * @param principal    - logged user
     * @return ResponseEntity with NO_CONTENT Http code
     * @throws ResponseStatusException is raised if user is not owner of selected recipe (it's id)
     */
    @PutMapping("api/recipe/{id}")
    public ResponseEntity<?> putRecipe(@PathVariable int id,
                                       @Valid @RequestBody Recipe recipeUpdate,
                                       Principal principal) throws ResponseStatusException {

        User user = userDetailsService.findUserByUsername(principal.getName());
        Recipe recipe = recipeService.findRecipeById(id);

        if (recipe.getUser().equals(user)) {
            recipeService.updateRecipeById(id, recipeUpdate);


            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }


}
