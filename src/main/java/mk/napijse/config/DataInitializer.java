package mk.napijse.config;

import mk.napijse.model.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.service.CategoryService;
import mk.napijse.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    public static final String ADMIN = "admin";

    private final UserService userService;
    private final CategoryService categoryService;

    public DataInitializer(UserService userService,
                           CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void initData() {
        User admin = this.userService.register(ADMIN, ADMIN, ADMIN, ADMIN, ADMIN, Role.ROLE_ADMIN);

        for (int i = 1; i < 6; i++) {
            this.categoryService.create("Category " + i, "Category description");
        }

    }
}
