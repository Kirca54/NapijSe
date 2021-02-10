package mk.napijse.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String ingredients;

    private LocalDateTime datePosted;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Category category;

    public Recipe(String name, String description, String ingredients, User createdBy, Category category) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.createdBy = createdBy;
        this.category = category;
        this.datePosted = LocalDateTime.now();
    }

    public Recipe() {
    }
}
