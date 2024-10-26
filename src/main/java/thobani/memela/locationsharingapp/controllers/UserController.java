package thobani.memela.locationsharingapp.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import thobani.memela.locationsharingapp.Database;
import thobani.memela.locationsharingapp.models.User;

public class UserController {
    public static void setupRoutes(Javalin app) {
        app.post("/register", UserController::registerUser);
        app.post("/login", UserController::loginUser);
    }

    // Registers a new user
    public static void registerUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            ctx.status(400).result("Username and password are required");
            return;
        }

        // Create and save the user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Password should ideally be hashed

        try {
            Database.saveUser(user);
            ctx.status(201).result("User registered successfully");
        } catch (Exception e) {
            ctx.status(500).result("Error registering user: " + e.getMessage());
        }
    }

    // Logs in a user by verifying their credentials
    public static void loginUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            ctx.status(400).result("Username and password are required");
            return;
        }

        User user = Database.getUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) { // Password check (ideally hashed)
            ctx.sessionAttribute("currentUser", user); // Store user in session
            ctx.status(200).result("Login successful");
        } else {
            ctx.status(401).result("Invalid username or password");
        }
    }
}
