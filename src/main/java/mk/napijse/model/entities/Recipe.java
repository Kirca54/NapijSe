package mk.napijse.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String ingredients;

    private Date datePosted;

    @ManyToOne
    private User recipeUser;

    @ManyToOne
    private Category category;

    public Recipe(String name, String description, String ingredients, User recipeUser, Category category) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.recipeUser = recipeUser;
        this.category = category;
        this.datePosted = Calendar.getInstance().getTime();
    }

    public Recipe() {
    }
}
