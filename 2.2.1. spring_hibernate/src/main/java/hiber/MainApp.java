package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.CarService;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MainApp {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);
        CarService carService = context.getBean(CarService.class);

        for(int counter = 1; counter < 6; ++counter) {
            Car car = new Car("Car", counter);
            User user = new User("User" + counter,
                    "Lastname" + counter,
                    "user" + counter + "@mail.ru",
                    car
            );

            car.setUser(user);

            carService.add(car);
            userService.add(user);
        }

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

        context.close();
    }
}
