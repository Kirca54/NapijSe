package mk.napijse.repository;

import mk.napijse.model.Comment;
import mk.napijse.model.Recipe;
import mk.napijse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRecipe(Recipe recipe);
    List<Comment> findAllByRecipeAndCommentUser(Recipe recipe, User user);
}
