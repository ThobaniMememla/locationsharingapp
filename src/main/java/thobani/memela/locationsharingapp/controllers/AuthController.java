package thobani.memela.locationsharingapp.controllers;

import io.javalin.http.Context;
import thobani.memela.locationsharingapp.Database;
import thobani.memela.locationsharingapp.models.User;

public class AuthController {

    // Handles user registration
    public static void registerUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            ctx.status(400).result("Username and password are required");
            return;
        }

        // Check if user already exists
        User existingUser = Database.getUserByUsername(username);
        if (existingUser != null) {
            ctx.status(409).result("Username already exists");
            return;
        }

        // Create a new user and store it in the database
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In a real app, hash the password here
        Database.saveUser(user);

        ctx.status(201).result("User registered successfully");
    }

    // Handles user login
    public static void loginUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            ctx.status(400).result("Username and password are required");
            return;
        }

        // Retrieve the user from the database
        User user = Database.getUserByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            ctx.status(401).result("Invalid username or password");
            return;
        }

        // Set the user session
        ctx.sessionAttribute("user", user);
        ctx.status(200).result("Login successful");
    }

    // Optional logout handler
    public static void logoutUser(Context ctx) {
        ctx.sessionAttribute("user", null);
        ctx.status(200).result("Logged out successfully");
    }
}
