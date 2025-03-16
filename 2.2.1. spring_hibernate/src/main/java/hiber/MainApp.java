package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MainApp {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);


        AtomicInteger counter = new AtomicInteger(1);
        Stream<User> userStream = Stream
                .generate(() -> new User("User" + counter,
                        "Lastname" + counter,
                        "user" + counter + "@mail.ru",
                        new Car("Car", counter.getAndIncrement())))
                .limit(5);

        userService.addAll(userStream.toList());

        List<User> users;

        users = userService.listUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }

        String model = "Car";
        int series = 2;

        User user = userService.getUserByCar(model, series);

        System.out.println(user != null
                ? "User found: " + user.getFirstName() + " " + user.getLastName() + "\n"
                : "User not found for car: " + model + " " + series + "\n");

        userService.cleanSchema();
        context.close();
    }
}
