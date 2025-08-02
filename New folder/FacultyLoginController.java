/*
  FACULTY LOGIN CONTROLLER - Complete application logic in a single file
  
  INSTRUCTIONS FOR NON-CODERS:
  - This file controls all the behavior of the login application
  - To change department options: Modify the DEPARTMENTS array
  - To change validation rules: Update the validateInput() method
  - To change colors: Modify the LIGHT_THEME and DARK_THEME arrays
  - To change login behavior: Update the performLogin() method
*/

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * FACULTY LOGIN APPLICATION CONTROLLER
 * 
 * This class contains all the logic for the faculty login application including:
 * - Theme management (light/dark mode)
 * - Department selection
 * - Login validation
 * - Password visibility toggle
 * - Remember me functionality
 * - Application startup
 */
public class FacultyLoginController extends Application implements Initializable {
    
    // DEPARTMENT CONFIGURATION - CHANGEABLE: Add or remove departments here
    private static final String[][] DEPARTMENTS = {
        {"BCA", "Bachelor of Computer Applications"},
        {"BBA", "Bachelor of Business Administration"}, 
        {"BCOM", "Bachelor of Commerce"}
    };
    
    // UI TEXT CONSTANTS - CHANGEABLE: Modify these for different text
    private static final String COLLEGE_NAME = "RNS First Grade College";
    private static final String SYSTEM_NAME = "Seminar Hall Booking System";
    private static final String COPYRIGHT_TEXT = "© 2024 RNS First Grade College. All rights reserved.";
    private static final String WELCOME_MESSAGE = "Faculty members, please select your department and log in to access the booking system";
    private static final String INSTRUCTIONS = "Select your department • Enter credentials • Access dashboard";
    
    // THEME CONFIGURATION - CHANGEABLE: Modify colors here
    private static final String LIGHT_THEME_CSS = """
        .root-container {
            -fx-background-color: linear-gradient(to bottom right, #ffffff 0%, #ffffff 70%, #e0e7ff 100%);
        }
        
        .header-section {
            -fx-background-color: rgba(255, 255, 255, 0.9);
            -fx-border-color: #e2e8f0;
            -fx-border-width: 0 0 1 0;
        }
        
        .college-name {
            -fx-font-family: "Georgia", serif;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #1e293b;
        }
        
        .system-name {
            -fx-font-size: 16px;
            -fx-font-weight: 500;
            -fx-text-fill: #64748b;
        }
        
        .theme-toggle {
            -fx-background-color: #f8fafc;
            -fx-border-color: #e2e8f0;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-size: 16px;
            -fx-padding: 8 12 8 12;
            -fx-cursor: hand;
        }
        
        .theme-toggle:hover {
            -fx-background-color: #f1f5f9;
            -fx-border-color: #cbd5e1;
        }
        
        .main-content {
            -fx-background-color: transparent;
        }
        
        .welcome-section {
            -fx-background-color: rgba(30, 58, 138, 0.05);
            -fx-border-color: rgba(30, 58, 138, 0.15);
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 20;
        }
        
        .welcome-text {
            -fx-font-size: 15px;
            -fx-text-fill: #64748b;
            -fx-wrap-text: true;
        }
        
        .instructions-text {
            -fx-font-size: 12px;
            -fx-text-fill: #94a3b8;
        }
        
        .login-card {
            -fx-background-color: rgba(255, 255, 255, 0.8);
            -fx-border-color: rgba(226, 232, 240, 0.6);
            -fx-border-radius: 15;
            -fx-background-radius: 15;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 20, 0, 0, 5);
        }
        
        .form-title {
            -fx-font-family: "Georgia", serif;
            -fx-font-size: 22px;
            -fx-font-weight: bold;
            -fx-text-fill: #1e293b;
        }
        
        .form-description {
            -fx-font-size: 13px;
            -fx-text-fill: #64748b;
        }
        
        .section-label {
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-text-fill: #374151;
        }
        
        .department-btn {
            -fx-background-color: #ffffff;
            -fx-border-color: #e2e8f0;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-text-fill: #64748b;
            -fx-padding: 12 20 12 20;
            -fx-cursor: hand;
            -fx-min-width: 80;
        }
        
        .department-btn:hover {
            -fx-background-color: #f8fafc;
            -fx-border-color: #cbd5e1;
            -fx-text-fill: #1e293b;
        }
        
        .department-btn-selected {
            -fx-background-color: #1e3a8a;
            -fx-border-color: #1e3a8a;
            -fx-text-fill: #ffffff;
        }
        
        .department-desc {
            -fx-font-size: 11px;
            -fx-text-fill: #64748b;
            -fx-font-style: italic;
        }
        
        .input-label {
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-text-fill: #374151;
        }
        
        .input-field, .password-field {
            -fx-background-color: rgba(248, 250, 252, 0.7);
            -fx-border-color: rgba(226, 232, 240, 0.8);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-padding: 12 15 12 15;
            -fx-pref-height: 45;
        }
        
        .input-field:focused, .password-field:focused {
            -fx-border-color: #1e3a8a;
            -fx-border-width: 2;
            -fx-background-color: rgba(255, 255, 255, 0.9);
        }
        
        .password-toggle {
            -fx-background-color: rgba(248, 250, 252, 0.7);
            -fx-border-color: rgba(226, 232, 240, 0.8);
            -fx-border-radius: 0 8 8 0;
            -fx-background-radius: 0 8 8 0;
            -fx-border-width: 1 1 1 0;
            -fx-font-size: 14px;
            -fx-padding: 8 12 8 12;
            -fx-cursor: hand;
        }
        
        .password-toggle:hover {
            -fx-background-color: #f1f5f9;
        }
        
        .remember-checkbox {
            -fx-font-size: 13px;
            -fx-text-fill: #374151;
        }
        
        .link-button {
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-text-fill: #1e3a8a;
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-cursor: hand;
            -fx-underline: false;
        }
        
        .link-button:hover {
            -fx-text-fill: #1d4ed8;
            -fx-underline: true;
        }
        
        .primary-button {
            -fx-background-color: #1e3a8a;
            -fx-border-color: #1e3a8a;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: #ffffff;
            -fx-font-size: 15px;
            -fx-font-weight: bold;
            -fx-padding: 12 0 12 0;
            -fx-cursor: hand;
            -fx-pref-height: 45;
        }
        
        .primary-button:hover {
            -fx-background-color: #1d4ed8;
            -fx-border-color: #1d4ed8;
        }
        
        .primary-button:disabled {
            -fx-background-color: #cbd5e1;
            -fx-border-color: #cbd5e1;
            -fx-text-fill: #94a3b8;
            -fx-opacity: 0.7;
        }
        
        .error-message {
            -fx-background-color: rgba(220, 38, 38, 0.1);
            -fx-border-color: rgba(220, 38, 38, 0.3);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: #dc2626;
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-padding: 10 15 10 15;
        }
        
        .loading-text {
            -fx-font-size: 14px;
            -fx-text-fill: #64748b;
            -fx-font-weight: 500;
        }
        
        .footer-section {
            -fx-background-color: transparent;
        }
        
        .footer-separator {
            -fx-background-color: linear-gradient(to right, transparent 0%, #e2e8f0 50%, transparent 100%);
        }
        
        .copyright-text {
            -fx-font-size: 12px;
            -fx-text-fill: #64748b;
        }
        """;
    
    private static final String DARK_THEME_CSS = """
        .root-container {
            -fx-background-color: linear-gradient(to bottom right, #0f172a 0%, #0f172a 70%, #1e1b4b 100%);
        }
        
        .header-section {
            -fx-background-color: rgba(30, 41, 59, 0.9);
            -fx-border-color: #334155;
            -fx-border-width: 0 0 1 0;
        }
        
        .college-name {
            -fx-font-family: "Georgia", serif;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #f8fafc;
        }
        
        .system-name {
            -fx-font-size: 16px;
            -fx-font-weight: 500;
            -fx-text-fill: #cbd5e1;
        }
        
        .theme-toggle {
            -fx-background-color: #1e293b;
            -fx-border-color: #334155;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-size: 16px;
            -fx-padding: 8 12 8 12;
            -fx-cursor: hand;
            -fx-text-fill: #cbd5e1;
        }
        
        .theme-toggle:hover {
            -fx-background-color: #334155;
            -fx-border-color: #475569;
        }
        
        .welcome-section {
            -fx-background-color: rgba(55, 48, 163, 0.15);
            -fx-border-color: rgba(55, 48, 163, 0.25);
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 20;
        }
        
        .welcome-text {
            -fx-font-size: 15px;
            -fx-text-fill: #cbd5e1;
            -fx-wrap-text: true;
        }
        
        .instructions-text {
            -fx-font-size: 12px;
            -fx-text-fill: #94a3b8;
        }
        
        .login-card {
            -fx-background-color: rgba(30, 41, 59, 0.8);
            -fx-border-color: rgba(51, 65, 85, 0.6);
            -fx-border-radius: 15;
            -fx-background-radius: 15;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 20, 0, 0, 5);
        }
        
        .form-title {
            -fx-font-family: "Georgia", serif;
            -fx-font-size: 22px;
            -fx-font-weight: bold;
            -fx-text-fill: #f8fafc;
        }
        
        .form-description {
            -fx-font-size: 13px;
            -fx-text-fill: #cbd5e1;
        }
        
        .section-label {
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-text-fill: #e2e8f0;
        }
        
        .department-btn {
            -fx-background-color: #1e293b;
            -fx-border-color: #334155;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-text-fill: #cbd5e1;
            -fx-padding: 12 20 12 20;
            -fx-cursor: hand;
            -fx-min-width: 80;
        }
        
        .department-btn:hover {
            -fx-background-color: #334155;
            -fx-border-color: #475569;
            -fx-text-fill: #f8fafc;
        }
        
        .department-btn-selected {
            -fx-background-color: #3730a3;
            -fx-border-color: #3730a3;
            -fx-text-fill: #ffffff;
        }
        
        .department-desc {
            -fx-font-size: 11px;
            -fx-text-fill: #cbd5e1;
            -fx-font-style: italic;
        }
        
        .input-label {
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-text-fill: #e2e8f0;
        }
        
        .input-field, .password-field {
            -fx-background-color: rgba(30, 41, 59, 0.7);
            -fx-border-color: rgba(51, 65, 85, 0.8);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-padding: 12 15 12 15;
            -fx-pref-height: 45;
            -fx-text-fill: #f8fafc;
        }
        
        .input-field:focused, .password-field:focused {
            -fx-border-color: #3730a3;
            -fx-border-width: 2;
            -fx-background-color: rgba(30, 41, 59, 0.9);
        }
        
        .password-toggle {
            -fx-background-color: rgba(30, 41, 59, 0.7);
            -fx-border-color: rgba(51, 65, 85, 0.8);
            -fx-border-radius: 0 8 8 0;
            -fx-background-radius: 0 8 8 0;
            -fx-border-width: 1 1 1 0;
            -fx-font-size: 14px;
            -fx-padding: 8 12 8 12;
            -fx-cursor: hand;
            -fx-text-fill: #cbd5e1;
        }
        
        .password-toggle:hover {
            -fx-background-color: #334155;
            -fx-text-fill: #f8fafc;
        }
        
        .remember-checkbox {
            -fx-font-size: 13px;
            -fx-text-fill: #e2e8f0;
        }
        
        .link-button {
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-text-fill: #a78bfa;
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-cursor: hand;
            -fx-underline: false;
        }
        
        .link-button:hover {
            -fx-text-fill: #c4b5fd;
            -fx-underline: true;
        }
        
        .primary-button {
            -fx-background-color: #3730a3;
            -fx-border-color: #3730a3;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: #ffffff;
            -fx-font-size: 15px;
            -fx-font-weight: bold;
            -fx-padding: 12 0 12 0;
            -fx-cursor: hand;
            -fx-pref-height: 45;
        }
        
        .primary-button:hover {
            -fx-background-color: #4338ca;
            -fx-border-color: #4338ca;
        }
        
        .primary-button:disabled {
            -fx-background-color: #475569;
            -fx-border-color: #475569;
            -fx-text-fill: #64748b;
            -fx-opacity: 0.7;
        }
        
        .error-message {
            -fx-background-color: rgba(239, 68, 68, 0.2);
            -fx-border-color: rgba(239, 68, 68, 0.4);
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-text-fill: #fca5a5;
            -fx-font-size: 13px;
            -fx-font-weight: 500;
            -fx-padding: 10 15 10 15;
        }
        
        .loading-text {
            -fx-font-size: 14px;
            -fx-text-fill: #cbd5e1;
            -fx-font-weight: 500;
        }
        
        .copyright-text {
            -fx-font-size: 12px;
            -fx-text-fill: #cbd5e1;
        }
        """;
    
    // FXML INJECTED FIELDS
    @FXML private ImageView collegeLogo;
    @FXML private Label collegeNameLabel;
    @FXML private Label systemNameLabel;
    @FXML private Label welcomeMessage;
    @FXML private Label instructionsLabel;
    @FXML private Label copyrightLabel;
    @FXML private Button themeToggleButton;
    @FXML private Button bcaButton;
    @FXML private Button bbaButton;
    @FXML private Button bcomButton;
    @FXML private Label departmentDescription;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button passwordToggle;
    @FXML private CheckBox rememberMeCheckbox;
    @FXML private Button forgotPasswordButton;
    @FXML private Button signInButton;
    @FXML private Label errorMessage;
    @FXML private javafx.scene.layout.HBox loadingIndicator;
    
    // INSTANCE VARIABLES
    private boolean isDarkTheme = false;
    private String selectedDepartment = "";
    private boolean isPasswordVisible = false;
    private boolean isLoading = false;
    private Preferences preferences;
    private Scene scene;
    
    /**
     * APPLICATION ENTRY POINT - Starts the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * START METHOD - Called when JavaFX application launches
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // CREATE CSS FILE IF IT DOESN'T EXIST
            createCSSFileIfNeeded();
            
            // LOAD FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyLogin.fxml"));
            Parent root = loader.load();
            
            // CREATE SCENE
            scene = new Scene(root, 1000, 700);
            
            // SETUP STAGE
            primaryStage.setTitle("Faculty Login - " + COLLEGE_NAME);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.centerOnScreen();
            
            // APPLY INITIAL THEME
            applyTheme();
            
            // SHOW STAGE
            primaryStage.show();
            
            System.out.println("✅ Faculty Login Application started successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error starting application: " + e.getMessage());
            e.printStackTrace();
            showAlert("Startup Error", "Failed to start the application: " + e.getMessage());
        }
    }
    
    /**
     * INITIALIZE METHOD - Called when FXML is loaded
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // SETUP PREFERENCES
            preferences = Preferences.userNodeForPackage(FacultyLoginController.class);
            
            // SETUP UI ELEMENTS
            setupCollegeInformation();
            setupPasswordToggle();
            setupFormValidation();
            loadSavedCredentials();
            loadThemePreference();
            
            System.out.println("✅ Controller initialized successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing controller: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * SETUP COLLEGE INFORMATION - Initialize college branding
     */
    private void setupCollegeInformation() {
        collegeNameLabel.setText(COLLEGE_NAME);
        systemNameLabel.setText(SYSTEM_NAME);
        welcomeMessage.setText(WELCOME_MESSAGE);
        instructionsLabel.setText(INSTRUCTIONS);
        copyrightLabel.setText(COPYRIGHT_TEXT);
        
        // SETUP LOGO (if available)
        try {
            Image logo = new Image(getClass().getResourceAsStream("college-logo.png"));
            collegeLogo.setImage(logo);
            System.out.println("✅ College logo loaded");
        } catch (Exception e) {
            System.out.println("⚠️ College logo not found - using placeholder");
            collegeLogo.setVisible(false);
        }
    }
    
    /**
     * SETUP PASSWORD TOGGLE - Initialize password visibility functionality
     */
    private void setupPasswordToggle() {
        // SYNC PASSWORD FIELDS
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
        updatePasswordToggleButton();
    }
    
    /**
     * SETUP FORM VALIDATION - Real-time form validation
     */
    private void setupFormValidation() {
        // UPDATE SIGN IN BUTTON STATE
        Runnable updateSignInButton = () -> {
            boolean isFormValid = !usernameField.getText().trim().isEmpty() &&
                                 !passwordField.getText().trim().isEmpty() &&
                                 !selectedDepartment.isEmpty() &&
                                 !isLoading;
            signInButton.setDisable(!isFormValid);
        };
        
        // ADD LISTENERS
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> updateSignInButton.run());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> updateSignInButton.run());
        
        // INITIAL CHECK
        updateSignInButton.run();
    }
    
    /**
     * LOAD SAVED CREDENTIALS - Load previously saved login information
     */
    private void loadSavedCredentials() {
        try {
            boolean wasRemembered = preferences.getBoolean("rememberMe", false);
            
            if (wasRemembered) {
                String savedUsername = preferences.get("savedUsername", "");
                String savedDepartment = preferences.get("savedDepartment", "");
                
                if (!savedUsername.isEmpty()) {
                    usernameField.setText(savedUsername);
                    rememberMeCheckbox.setSelected(true);
                    
                    if (!savedDepartment.isEmpty()) {
                        selectDepartmentById(savedDepartment);
                    }
                    
                    System.out.println("📋 Loaded saved credentials for: " + savedUsername);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading saved credentials: " + e.getMessage());
        }
    }
    
    /**
     * LOAD THEME PREFERENCE - Load saved theme preference
     */
    private void loadThemePreference() {
        try {
            isDarkTheme = preferences.getBoolean("isDarkTheme", false);
            updateThemeToggleButton();
            System.out.println("🎨 Loaded theme preference: " + (isDarkTheme ? "Dark" : "Light"));
        } catch (Exception e) {
            System.err.println("❌ Error loading theme preference: " + e.getMessage());
        }
    }
    
    /**
     * DEPARTMENT SELECTION HANDLERS
     */
    @FXML
    private void selectBCA() {
        selectDepartment("BCA", "Bachelor of Computer Applications");
    }
    
    @FXML
    private void selectBBA() {
        selectDepartment("BBA", "Bachelor of Business Administration");
    }
    
    @FXML
    private void selectBCOM() {
        selectDepartment("BCOM", "Bachelor of Commerce");
    }
    
    /**
     * SELECT DEPARTMENT - Handle department selection
     */
    private void selectDepartment(String deptId, String deptFullName) {
        selectedDepartment = deptId;
        
        // UPDATE BUTTON STYLES
        bcaButton.getStyleClass().removeAll("department-btn-selected");
        bbaButton.getStyleClass().removeAll("department-btn-selected");
        bcomButton.getStyleClass().removeAll("department-btn-selected");
        
        switch (deptId) {
            case "BCA":
                bcaButton.getStyleClass().add("department-btn-selected");
                break;
            case "BBA":
                bbaButton.getStyleClass().add("department-btn-selected");
                break;
            case "BCOM":
                bcomButton.getStyleClass().add("department-btn-selected");
                break;
        }
        
        // SHOW DEPARTMENT DESCRIPTION
        departmentDescription.setText(deptFullName);
        departmentDescription.setVisible(true);
        
        // CLEAR ERROR MESSAGE
        hideErrorMessage();
        
        System.out.println("🏫 Selected department: " + deptId);
    }
    
    /**
     * SELECT DEPARTMENT BY ID - Helper method for loading saved department
     */
    private void selectDepartmentById(String deptId) {
        for (String[] dept : DEPARTMENTS) {
            if (dept[0].equals(deptId)) {
                selectDepartment(dept[0], dept[1]);
                break;
            }
        }
    }
    
    /**
     * TOGGLE PASSWORD VISIBILITY - Show/hide password
     */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        
        passwordField.setVisible(!isPasswordVisible);
        passwordVisibleField.setVisible(isPasswordVisible);
        
        // FOCUS THE VISIBLE FIELD
        if (isPasswordVisible) {
            passwordVisibleField.requestFocus();
            passwordVisibleField.positionCaret(passwordVisibleField.getText().length());
        } else {
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
        
        updatePasswordToggleButton();
    }
    
    /**
     * UPDATE PASSWORD TOGGLE BUTTON - Update button appearance
     */
    private void updatePasswordToggleButton() {
        if (isPasswordVisible) {
            passwordToggle.setText("🙈");
        } else {
            passwordToggle.setText("👁");
        }
    }
    
    /**
     * HANDLE SIGN IN - Process login attempt
     */
    @FXML
    private void handleSignIn() {
        try {
            // VALIDATE INPUT
            if (!validateInput()) {
                return;
            }
            
            // START LOADING STATE
            setLoadingState(true);
            
            // GET FORM DATA
            String username = usernameField.getText().trim();
            String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
            String department = selectedDepartment;
            boolean rememberMe = rememberMeCheckbox.isSelected();
            
            // CREATE BACKGROUND TASK FOR LOGIN
            Task<Boolean> loginTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    // SIMULATE NETWORK DELAY
                    Thread.sleep(1500);
                    
                    // PERFORM LOGIN VALIDATION
                    return performLogin(username, password, department);
                }
                
                @Override
                protected void succeeded() {
                    setLoadingState(false);
                    boolean loginSuccess = getValue();
                    
                    if (loginSuccess) {
                        handleLoginSuccess(username, department, rememberMe);
                    } else {
                        showErrorMessage("Invalid credentials. Please check your username, password, and department.");
                    }
                }
                
                @Override
                protected void failed() {
                    setLoadingState(false);
                    showErrorMessage("Login failed. Please try again.");
                    System.err.println("❌ Login task failed: " + getException().getMessage());
                }
            };
            
            // RUN LOGIN TASK
            new Thread(loginTask).start();
            
        } catch (Exception e) {
            setLoadingState(false);
            showErrorMessage("An error occurred during login. Please try again.");
            System.err.println("❌ Error handling sign in: " + e.getMessage());
        }
    }
    
    /**
     * VALIDATE INPUT - Check form inputs
     */
    private boolean validateInput() {
        if (selectedDepartment.isEmpty()) {
            showErrorMessage("Please select your department.");
            return false;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showErrorMessage("Please enter your username.");
            usernameField.requestFocus();
            return false;
        }
        
        String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
        if (password.trim().isEmpty()) {
            showErrorMessage("Please enter your password.");
            if (isPasswordVisible) {
                passwordVisibleField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * PERFORM LOGIN - Actual login validation
     * TODO: Replace with actual authentication service
     */
    private boolean performLogin(String username, String password, String department) {
        System.out.println("🔐 Attempting login for: " + username + " (" + department + ")");
        
        // SIMPLE VALIDATION FOR DEMO (REPLACE IN PRODUCTION)
        // In a real application, this would call your authentication service
        return !username.isEmpty() && !password.isEmpty() && !department.isEmpty();
    }
    
    /**
     * HANDLE LOGIN SUCCESS - Process successful login
     */
    private void handleLoginSuccess(String username, String department, boolean rememberMe) {
        try {
            // HANDLE REMEMBER ME
            if (rememberMe) {
                preferences.put("savedUsername", username);
                preferences.put("savedDepartment", selectedDepartment);
                preferences.putBoolean("rememberMe", true);
            } else {
                preferences.remove("savedUsername");
                preferences.remove("savedDepartment");
                preferences.putBoolean("rememberMe", false);
            }
            preferences.flush();
            
            // SHOW SUCCESS MESSAGE
            showAlert("Login Successful", 
                     "Welcome " + username + " from " + department + " department!\n\n" +
                     "You have successfully logged into the seminar hall booking system.");
            
            System.out.println("✅ Login successful for: " + username + " (" + department + ")");
            
        } catch (Exception e) {
            System.err.println("❌ Error handling login success: " + e.getMessage());
        }
    }
    
    /**
     * HANDLE FORGOT PASSWORD - Handle forgot password link
     */
    @FXML
    private void handleForgotPassword() {
        showAlert("Forgot Password", 
                 "Password Reset Information\n\n" +
                 "Please contact the IT administrator to reset your password:\n\n" +
                 "📧 Email: it-support@rnscollege.edu\n" +
                 "📞 Phone: +91-XXX-XXX-XXXX\n" +
                 "🕒 Office Hours: 9:00 AM - 5:00 PM");
    }
    
    /**
     * TOGGLE THEME - Switch between light and dark themes
     */
    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        updateThemeToggleButton();
        applyTheme();
        saveThemePreference();
        
        System.out.println("🎨 Theme toggled to: " + (isDarkTheme ? "Dark" : "Light"));
    }
    
    /**
     * UPDATE THEME TOGGLE BUTTON - Update button appearance
     */
    private void updateThemeToggleButton() {
        if (isDarkTheme) {
            themeToggleButton.setText("☀️");
        } else {
            themeToggleButton.setText("🌙");
        }
    }
    
    /**
     * APPLY THEME - Apply current theme to the scene
     */
    private void applyTheme() {
        if (scene != null) {
            try {
                // CREATE TEMPORARY CSS FILE
                String cssContent = isDarkTheme ? DARK_THEME_CSS : LIGHT_THEME_CSS;
                writeCSS(cssContent);
                
                // REFRESH STYLESHEET
                scene.getStylesheets().clear();
                scene.getStylesheets().add("file:faculty-login-styles.css");
                
            } catch (Exception e) {
                System.err.println("❌ Error applying theme: " + e.getMessage());
            }
        }
    }
    
    /**
     * SAVE THEME PREFERENCE - Save current theme to preferences
     */
    private void saveThemePreference() {
        try {
            preferences.putBoolean("isDarkTheme", isDarkTheme);
            preferences.flush();
        } catch (Exception e) {
            System.err.println("❌ Error saving theme preference: " + e.getMessage());
        }
    }
    
    /**
     * CREATE CSS FILE IF NEEDED - Create the CSS file if it doesn't exist
     */
    private void createCSSFileIfNeeded() {
        try {
            if (!Files.exists(Paths.get("faculty-login-styles.css"))) {
                writeCSS(LIGHT_THEME_CSS);
                System.out.println("✅ Created CSS file");
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating CSS file: " + e.getMessage());
        }
    }
    
    /**
     * WRITE CSS - Write CSS content to file
     */
    private void writeCSS(String cssContent) throws IOException {
        try (FileWriter writer = new FileWriter("faculty-login-styles.css")) {
            writer.write(cssContent);
        }
    }
    
    /**
     * SET LOADING STATE - Update UI for loading state
     */
    private void setLoadingState(boolean loading) {
        isLoading = loading;
        
        // UPDATE SIGN IN BUTTON
        signInButton.setText(loading ? "Signing In..." : "Sign In");
        signInButton.setDisable(loading);
        
        // SHOW/HIDE LOADING INDICATOR
        loadingIndicator.setVisible(loading);
        
        // DISABLE FORM FIELDS
        usernameField.setDisable(loading);
        passwordField.setDisable(loading);
        passwordVisibleField.setDisable(loading);
        passwordToggle.setDisable(loading);
        rememberMeCheckbox.setDisable(loading);
        forgotPasswordButton.setDisable(loading);
        bcaButton.setDisable(loading);
        bbaButton.setDisable(loading);
        bcomButton.setDisable(loading);
        themeToggleButton.setDisable(loading);
    }
    
    /**
     * SHOW ERROR MESSAGE - Display error message
     */
    private void showErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        
        // AUTO-HIDE AFTER 5 SECONDS
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> hideErrorMessage());
        pause.play();
    }
    
    /**
     * HIDE ERROR MESSAGE - Hide error message
     */
    private void hideErrorMessage() {
        errorMessage.setVisible(false);
    }
    
    /**
     * SHOW ALERT - Display alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. CHANGING COLLEGE INFORMATION:
     - Modify COLLEGE_NAME, SYSTEM_NAME, and COPYRIGHT_TEXT constants
     - Update WELCOME_MESSAGE and INSTRUCTIONS for different text
  
  2. CHANGING DEPARTMENTS:
     - Modify the DEPARTMENTS array with your department codes and full names
     - Each entry needs: {"CODE", "Full Department Name"}
  
  3. CHANGING COLORS:
     - Modify LIGHT_THEME_CSS and DARK_THEME_CSS strings
     - Look for color values like #1e3a8a and replace with your colors
     - Use online color picker tools to find hex color codes
  
  4. CHANGING VALIDATION:
     - Modify validateInput() method to add/remove validation rules
     - Update performLogin() method for actual authentication
  
  5. CHANGING BEHAVIOR:
     - Modify handleLoginSuccess() for post-login actions
     - Update handleForgotPassword() for your password reset process
  
  6. ADDING FEATURES:
     - Add new @FXML methods for new buttons/controls
     - Add corresponding elements to the FXML file
     - Update initialization and validation logic as needed
*/