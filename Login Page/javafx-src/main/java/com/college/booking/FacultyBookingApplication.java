/*
  MAIN JAVAFX APPLICATION - Entry point for the Faculty Login System
  
  INSTRUCTIONS FOR NON-CODERS:
  - This is the main class that starts the JavaFX application
  - To change the application title: Look for "setTitle" and modify the text
  - To change window size: Look for "setWidth" and "setHeight" values
  - To change the icon: Replace the icon path in "getIcons().add()"
  
  SETUP REQUIREMENTS:
  - Java 11 or higher
  - JavaFX 17 or higher
  - Place this in src/main/java/com/college/booking/
*/

package com.college.booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * MAIN APPLICATION CLASS
 * This class launches the JavaFX application and sets up the primary window
 */
public class FacultyBookingApplication extends Application {
    
    // APPLICATION CONSTANTS - CHANGEABLE: Modify these values as needed
    private static final String APPLICATION_TITLE = "RNS First Grade College - Seminar Hall Booking System";
    private static final double WINDOW_WIDTH = 1200;        // Application window width
    private static final double WINDOW_HEIGHT = 800;        // Application window height
    private static final double MIN_WIDTH = 900;            // Minimum window width
    private static final double MIN_HEIGHT = 600;           // Minimum window height
    
    // STATIC REFERENCES - Used throughout the application
    private static Stage primaryStage;
    private static ThemeManager themeManager;
    
    /**
     * START METHOD - Called when JavaFX application launches
     * This method sets up the main window and loads the initial scene
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        // INITIALIZE THEME MANAGER
        themeManager = new ThemeManager();
        
        // LOAD MAIN SCENE
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        
        // CREATE SCENE WITH LOADED FXML
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // APPLY INITIAL THEME
        themeManager.applyTheme(scene, themeManager.getCurrentTheme());
        
        // CONFIGURE PRIMARY STAGE
        stage.setTitle(APPLICATION_TITLE);                   // CHANGEABLE: Application title
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);                        // Minimum window width
        stage.setMinHeight(MIN_HEIGHT);                      // Minimum window height
        stage.setResizable(true);                           // Allow window resizing
        
        // ADD APPLICATION ICON - CHANGEABLE: Replace with your college logo
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/college-logo.png")));
        } catch (Exception e) {
            System.out.println("Warning: Could not load application icon - " + e.getMessage());
        }
        
        // CENTER WINDOW ON SCREEN
        stage.centerOnScreen();
        
        // SHOW THE APPLICATION WINDOW
        stage.show();
        
        // LOG APPLICATION START
        System.out.println("🎓 Faculty Booking System started successfully!");
        System.out.println("📊 Window size: " + WINDOW_WIDTH + "x" + WINDOW_HEIGHT);
        System.out.println("🎨 Theme: " + themeManager.getCurrentTheme());
    }
    
    /**
     * STOP METHOD - Called when application is closing
     * Cleanup resources and save user preferences
     */
    @Override
    public void stop() throws Exception {
        // SAVE THEME PREFERENCE
        if (themeManager != null) {
            themeManager.saveThemePreference();
        }
        
        // LOG APPLICATION SHUTDOWN
        System.out.println("👋 Faculty Booking System shutting down...");
        
        super.stop();
    }
    
    /**
     * GETTER METHODS - Provide access to static references
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static ThemeManager getThemeManager() {
        return themeManager;
    }
    
    /**
     * MAIN METHOD - Entry point for the application
     */
    public static void main(String[] args) {
        // SET SYSTEM PROPERTIES FOR BETTER RENDERING
        System.setProperty("javafx.preloader", "com.college.booking.AppPreloader");
        System.setProperty("prism.lcdtext", "false");        // Better text rendering
        System.setProperty("prism.text", "t2k");             // Text rendering optimization
        
        // LAUNCH JAVAFX APPLICATION
        launch(args);
    }
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. CHANGING APPLICATION DETAILS:
     - Application title: Modify APPLICATION_TITLE constant
     - Window size: Change WINDOW_WIDTH and WINDOW_HEIGHT
     - Minimum size: Adjust MIN_WIDTH and MIN_HEIGHT
  
  2. ADDING APPLICATION ICON:
     - Place your college logo in src/main/resources/images/
     - Name it "college-logo.png" or update the path in getIcons().add()
     - Supported formats: PNG, JPG, GIF
  
  3. CHANGING STARTUP BEHAVIOR:
     - Window positioning: Modify stage.centerOnScreen() or set specific coordinates
     - Window state: Add stage.setMaximized(true) for fullscreen start
     - Window decoration: Change stage.initStyle(StageStyle.UNDECORATED) for borderless
  
  4. ADDING STARTUP LOGIC:
     - Add initialization code in the start() method
     - Database connections, user preference loading, etc.
  
  5. PERFORMANCE OPTIMIZATION:
     - Modify system properties in main() method for better rendering
     - Add JVM arguments for memory optimization
  
  6. ERROR HANDLING:
     - Add try-catch blocks around critical initialization code
     - Implement error dialogs for startup failures
*/