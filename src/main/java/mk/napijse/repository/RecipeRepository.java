package mk.napijse.repository;

import mk.napijse.model.entities.Category;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Recipe> findAllByCategory(Category category, Pageable pageable);
    Page<Recipe> findAllByNameContainingIgnoreCaseAndCategory(String name, Category category, Pageable pageable);
    List<Recipe> findAllByRecipeUser(User user);
}
