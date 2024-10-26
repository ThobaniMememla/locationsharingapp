package thobani.memela.locationsharingapp.controllers;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import thobani.memela.locationsharingapp.Database;

import java.sql.*;
import java.util.Map;

public class LocationController {
    public static void setupRoutes(Javalin app) {
        app.post("/update-location", ctx -> {
            int userId = ctx.sessionAttribute("userId");
            String body = ctx.body();
            double latitude = Double.parseDouble(ctx.formParam("latitude"));
            double longitude = Double.parseDouble(ctx.formParam("longitude"));

            try (Connection conn = DriverManager.getConnection(Database.URL)) {
                String sql = "UPDATE Users SET latitude = ?, longitude = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, latitude);
                pstmt.setDouble(2, longitude);
                pstmt.setInt(3, userId);
                pstmt.executeUpdate();
                ctx.status(200).result("Location updated");
            }
        });

        app.get("/get-location/:userId", ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));

            try (Connection conn = DriverManager.getConnection(Database.URL)) {
                String sql = "SELECT latitude, longitude FROM Users WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");
                    ctx.json(Map.of("latitude", latitude, "longitude", longitude));
                } else {
                    ctx.status(404).result("User not found");
                }
            }
        });
    }
}
