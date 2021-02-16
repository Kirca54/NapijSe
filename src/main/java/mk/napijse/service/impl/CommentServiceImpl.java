package mk.napijse.service.impl;

import mk.napijse.model.Comment;
import mk.napijse.model.Recipe;
import mk.napijse.model.User;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.model.exceptions.UserNotFoundException;
import mk.napijse.repository.CommentRepository;
import mk.napijse.repository.RecipeRepository;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.CommentService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              RecipeRepository recipeRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    @Override
    public List<Comment> findAllByRecipe(Long recipeId) {
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        return this.commentRepository.findAllByRecipe(recipe);
    }

    @Override
    public List<Comment> findAllByRecipeAndUsername(Long recipeId, String username) {
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return this.commentRepository.findAllByRecipeAndCommentUser(recipe, user);
    }

    @Override
    public Comment save(Long recipeId, String username, String content) {
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        User user = this.userRepository.findByUsername(username).get();
        Comment comment = new Comment(content, user, recipe);
        return this.commentRepository.save(comment);
    }
}
