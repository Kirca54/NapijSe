package mk.napijse.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Date datePosted;

    @ManyToOne
    private User commentUser;

    @ManyToOne
    private Recipe recipe;

    public Comment() {
    }

    public Comment(String content, User commentUser, Recipe recipe) {
        this.content = content;
        this.commentUser = commentUser;
        this.recipe = recipe;
        this.datePosted = Calendar.getInstance().getTime();
    }
}
