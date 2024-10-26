package thobani.memela.locationsharingapp;

import io.javalin.Javalin;
import thobani.memela.locationsharingapp.controllers.LocationController;
import thobani.memela.locationsharingapp.controllers.UserController;

public class App {
    public static void main(String[] args) {
        // Initialize Javalin app
        Javalin app = Javalin.create(config -> {
            // config.addStaticFiles("/public"); // Serve static files like HTML, CSS, and JS from /public
        }).start(7000);

        // Initialize the database (creates tables if they don't exist)
        Database.init();

        // Set up routes
        UserController.setupRoutes(app);
        LocationController.setupRoutes(app);

        // Root route redirecting to the login page
        app.get("/", ctx -> ctx.redirect("/login.html"));
    }
}
