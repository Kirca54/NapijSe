package mk.napijse.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime datePosted;

    @OneToOne
    private User username;

    @OneToOne
    private Recipe recipe;

    public Comment() {
    }

    public Comment(String content, User username, Recipe recipe) {
        this.content = content;
        this.username = username;
        this.recipe = recipe;
        this.datePosted = LocalDateTime.now();
    }
}
