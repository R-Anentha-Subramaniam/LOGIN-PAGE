/*
  MAIN CONTROLLER CLASS - Controls the main application window and navigation
  
  INSTRUCTIONS FOR NON-CODERS:
  - This class controls the main application window behavior
  - To change default view: Modify INITIAL_VIEW constant
  - To change college information: Update the label text in initialize() method
  - To change logo: Replace the logo file in resources/images/
*/

package com.college.booking.controllers;

import com.college.booking.FacultyBookingApplication;
import com.college.booking.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MAIN CONTROLLER CLASS
 * Controls the main application window, navigation between forms, and theme switching
 */
public class MainController implements Initializable {
    
    // VIEW ENUMERATION - Defines available application views
    public enum View {
        LOGIN("login", "Login", "/fxml/LoginForm.fxml"),
        SIGNUP("signup", "Signup", "/fxml/SignupForm.fxml");
        
        private final String id;
        private final String displayName;
        private final String fxmlPath;
        
        View(String id, String displayName, String fxmlPath) {
            this.id = id;
            this.displayName = displayName;
            this.fxmlPath = fxmlPath;
        }
        
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getFxmlPath() { return fxmlPath; }
    }
    
    // CONSTANTS - CHANGEABLE: Modify these values as needed
    private static final View INITIAL_VIEW = View.LOGIN;
    private static final String COLLEGE_NAME = "RNS First Grade College";           // CHANGEABLE
    private static final String SYSTEM_NAME = "Seminar Hall Booking System";       // CHANGEABLE
    private static final String COPYRIGHT_TEXT = "© 2024 RNS First Grade College. All rights reserved."; // CHANGEABLE
    private static final String LOGO_PATH = "/images/college-logo.png";            // CHANGEABLE
    
    // FXML INJECTED FIELDS - Connected to FXML elements
    @FXML private ImageView collegeLogo;
    @FXML private Label collegeNameLabel;
    @FXML private Label systemNameLabel;
    @FXML private Label welcomeMessageLabel;
    @FXML private Label instructionsLabel;
    @FXML private Label copyrightLabel;
    @FXML private Button themeToggleButton;
    @FXML private VBox contentContainer;
    @FXML private StackPane formContainer;
    @FXML private HBox navigationSection;
    
    // INSTANCE VARIABLES
    private View currentView;
    private ThemeManager themeManager;
    
    /**
     * INITIALIZE METHOD - Called automatically when FXML is loaded
     * Sets up the initial state of the application
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // GET THEME MANAGER REFERENCE
            themeManager = FacultyBookingApplication.getThemeManager();
            
            // SETUP COLLEGE INFORMATION
            setupCollegeInformation();
            
            // SETUP THEME TOGGLE BUTTON
            setupThemeToggle();
            
            // LOAD INITIAL VIEW
            switchToView(INITIAL_VIEW);
            
            System.out.println("✅ Main Controller initialized successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing Main Controller: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * SETUP COLLEGE INFORMATION - Initialize college logo and text
     */
    private void setupCollegeInformation() {
        try {
            // SET COLLEGE TEXT INFORMATION
            collegeNameLabel.setText(COLLEGE_NAME);
            systemNameLabel.setText(SYSTEM_NAME);
            copyrightLabel.setText(COPYRIGHT_TEXT);
            
            // LOAD COLLEGE LOGO
            try {
                Image logoImage = new Image(getClass().getResourceAsStream(LOGO_PATH));
                collegeLogo.setImage(logoImage);
                System.out.println("✅ College logo loaded successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Could not load college logo: " + e.getMessage());
                // Hide logo if it can't be loaded
                collegeLogo.setVisible(false);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error setting up college information: " + e.getMessage());
        }
    }
    
    /**
     * SETUP THEME TOGGLE - Initialize theme toggle button
     */
    private void setupThemeToggle() {
        updateThemeToggleButton();
    }
    
    /**
     * UPDATE THEME TOGGLE BUTTON - Update button appearance based on current theme
     */
    private void updateThemeToggleButton() {
        if (themeManager != null) {
            if (themeManager.isDarkMode()) {
                themeToggleButton.setText("☀️");  // Sun icon for light mode toggle
                themeToggleButton.setAccessibleText("Switch to light mode");
            } else {
                themeToggleButton.setText("🌙");  // Moon icon for dark mode toggle
                themeToggleButton.setAccessibleText("Switch to dark mode");
            }
        }
    }
    
    /**
     * TOGGLE THEME - Handle theme toggle button click
     */
    @FXML
    private void toggleTheme() {
        try {
            if (themeManager != null) {
                themeManager.toggleTheme(themeToggleButton.getScene());
                updateThemeToggleButton();
                System.out.println("🎨 Theme toggled to: " + themeManager.getCurrentTheme().getDisplayName());
            }
        } catch (Exception e) {
            System.err.println("❌ Error toggling theme: " + e.getMessage());
        }
    }
    
    /**
     * SWITCH TO VIEW - Load and display the specified view
     * 
     * @param view The view to switch to (LOGIN or SIGNUP)
     */
    public void switchToView(View view) {
        try {
            // LOAD FXML FOR THE VIEW
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));
            Node formNode = loader.load();
            
            // GET CONTROLLER AND SET PARENT REFERENCE
            Object controller = loader.getController();
            if (controller instanceof LoginController) {
                ((LoginController) controller).setMainController(this);
            } else if (controller instanceof SignupController) {
                ((SignupController) controller).setMainController(this);
            }
            
            // CLEAR AND ADD NEW FORM
            formContainer.getChildren().clear();
            formContainer.getChildren().add(formNode);
            
            // UPDATE CURRENT VIEW
            currentView = view;
            
            // UPDATE WELCOME MESSAGE AND INSTRUCTIONS
            updateWelcomeContent(view);
            
            // UPDATE NAVIGATION SECTION
            updateNavigationSection(view);
            
            System.out.println("✅ Switched to " + view.getDisplayName() + " view");
            
        } catch (IOException e) {
            System.err.println("❌ Error loading " + view.getDisplayName() + " view: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * UPDATE WELCOME CONTENT - Update welcome message based on current view
     */
    private void updateWelcomeContent(View view) {
        switch (view) {
            case LOGIN:
                welcomeMessageLabel.setText("Faculty members, please select your department and log in to access the booking system");
                instructionsLabel.setText("Select your department • Enter credentials • Access dashboard");
                break;
            case SIGNUP:
                welcomeMessageLabel.setText("New faculty members, please fill out the registration form to create your account");
                instructionsLabel.setText("Fill personal details • Add professional info • Create account");
                break;
        }
    }
    
    /**
     * UPDATE NAVIGATION SECTION - Update navigation links based on current view
     */
    private void updateNavigationSection(View view) {
        navigationSection.getChildren().clear();
        
        switch (view) {
            case LOGIN:
                // ADD SIGNUP LINK
                Label signupPrompt = new Label("New faculty member? ");
                signupPrompt.getStyleClass().add("navigation-text");
                
                Button signupLink = new Button("Create an account");
                signupLink.getStyleClass().addAll("link-button", "navigation-link");
                signupLink.setOnAction(e -> switchToView(View.SIGNUP));
                
                navigationSection.getChildren().addAll(signupPrompt, signupLink);
                break;
                
            case SIGNUP:
                // ADD LOGIN LINK
                Label loginPrompt = new Label("Already have an account? ");
                loginPrompt.getStyleClass().add("navigation-text");
                
                Button loginLink = new Button("Sign in");
                loginLink.getStyleClass().addAll("link-button", "navigation-link");
                loginLink.setOnAction(e -> switchToView(View.LOGIN));
                
                navigationSection.getChildren().addAll(loginPrompt, loginLink);
                break;
        }
    }
    
    /**
     * HANDLE SUCCESSFUL LOGIN - Called when login is successful
     * 
     * @param username The username of the logged-in user
     * @param department The department of the logged-in user
     */
    public void handleSuccessfulLogin(String username, String department) {
        try {
            // TODO: Navigate to dashboard or main application view
            System.out.println("🎉 Login successful for: " + username + " (" + department + ")");
            
            // FOR NOW, SHOW SUCCESS MESSAGE
            // In a real application, this would load the dashboard FXML
            showSuccessMessage("Login successful! Welcome " + username + " from " + department + " department.");
            
        } catch (Exception e) {
            System.err.println("❌ Error handling successful login: " + e.getMessage());
        }
    }
    
    /**
     * HANDLE SUCCESSFUL SIGNUP - Called when signup is successful
     * 
     * @param username The username of the newly registered user
     * @param email The email of the newly registered user
     */
    public void handleSuccessfulSignup(String username, String email) {
        try {
            System.out.println("🎉 Signup successful for: " + username + " (" + email + ")");
            
            // SHOW SUCCESS MESSAGE AND SWITCH TO LOGIN
            showSuccessMessage("Account created successfully! Please wait for admin approval.");
            
            // SWITCH BACK TO LOGIN VIEW AFTER DELAY
            javafx.concurrent.Task<Void> delayTask = new javafx.concurrent.Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(2000); // 2 second delay
                    return null;
                }
                
                @Override
                protected void succeeded() {
                    switchToView(View.LOGIN);
                }
            };
            
            new Thread(delayTask).start();
            
        } catch (Exception e) {
            System.err.println("❌ Error handling successful signup: " + e.getMessage());
        }
    }
    
    /**
     * SHOW SUCCESS MESSAGE - Display a success message to the user
     * TODO: Replace with proper dialog or notification system
     */
    private void showSuccessMessage(String message) {
        // PLACEHOLDER IMPLEMENTATION
        // In a real application, this would show a proper dialog or notification
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * GETTER METHODS
     */
    public View getCurrentView() {
        return currentView;
    }
    
    public ThemeManager getThemeManager() {
        return themeManager;
    }
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. CHANGING COLLEGE INFORMATION:
     - Modify COLLEGE_NAME constant for college name
     - Modify SYSTEM_NAME constant for system description
     - Modify COPYRIGHT_TEXT for footer copyright
     - Update LOGO_PATH to point to your logo file
  
  2. CHANGING DEFAULT VIEW:
     - Modify INITIAL_VIEW constant (View.LOGIN or View.SIGNUP)
     - Application will start with the specified view
  
  3. ADDING NEW VIEWS:
     - Add new entries to the View enum
     - Create corresponding FXML and controller files
     - Add switch cases in switchToView() method
  
  4. CUSTOMIZING WELCOME MESSAGES:
     - Modify text in updateWelcomeContent() method
     - Add new cases for additional views
  
  5. CHANGING NAVIGATION:
     - Modify updateNavigationSection() method
     - Change button text and behavior
  
  6. SUCCESS HANDLING:
     - Modify handleSuccessfulLogin() and handleSuccessfulSignup()
     - Replace placeholder alerts with proper UI
     - Add navigation to dashboard or other views
  
  7. THEME CUSTOMIZATION:
     - Theme toggle icons can be changed in updateThemeToggleButton()
     - Add more theme options by extending ThemeManager
*/