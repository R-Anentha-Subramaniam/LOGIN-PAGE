/*
  LOGIN CONTROLLER CLASS - Handles faculty login form behavior
  
  INSTRUCTIONS FOR NON-CODERS:
  - This class controls the login form behavior and validation
  - To change department options: Modify DEPARTMENTS array
  - To change validation rules: Modify validateForm() method
  - To change UI text: Modify the string constants at the top
*/

package com.college.booking.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * LOGIN CONTROLLER CLASS
 * Handles the faculty login form functionality including validation and authentication
 */
public class LoginController implements Initializable {
    
    // DEPARTMENT CONFIGURATION - CHANGEABLE: Add or remove departments here
    private static final Department[] DEPARTMENTS = {
        new Department("BCA", "BCA", "Bachelor of Computer Applications"),
        new Department("BBA", "BBA", "Bachelor of Business Administration"),
        new Department("BCOM", "BCOM", "Bachelor of Commerce")
    };
    
    // UI TEXT CONSTANTS - CHANGEABLE: Modify these for different text
    private static final String FORM_TITLE = "Faculty Login";
    private static final String FORM_DESCRIPTION = "Access the seminar hall booking system";
    private static final String USERNAME_PLACEHOLDER = "Enter your faculty username";
    private static final String PASSWORD_PLACEHOLDER = "Enter your password";
    private static final String SIGN_IN_TEXT = "Sign In";
    private static final String SIGNING_IN_TEXT = "Signing In...";
    private static final String REMEMBER_ME_TEXT = "Remember me";
    private static final String FORGOT_PASSWORD_TEXT = "Forgot password?";
    
    // PREFERENCE KEYS
    private static final String PREF_USERNAME = "rememberedUsername";
    private static final String PREF_DEPARTMENT = "rememberedDepartment";
    private static final String PREF_REMEMBER_ME = "rememberMe";
    
    // FXML INJECTED FIELDS
    @FXML private GridPane departmentGrid;
    @FXML private Label selectedDepartmentLabel;
    @FXML private Label errorMessageLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button passwordToggleButton;
    @FXML private CheckBox rememberMeCheckbox;
    @FXML private Button forgotPasswordButton;
    @FXML private Button signInButton;
    @FXML private VBox loadingContainer;
    
    // INSTANCE VARIABLES
    private MainController mainController;
    private String selectedDepartmentId = "";
    private boolean isPasswordVisible = false;
    private boolean isLoading = false;
    private Preferences preferences;
    
    /**
     * DEPARTMENT DATA CLASS
     */
    private static class Department {
        final String id;
        final String name;
        final String fullName;
        
        Department(String id, String name, String fullName) {
            this.id = id;
            this.name = name;
            this.fullName = fullName;
        }
    }
    
    /**
     * INITIALIZE METHOD - Called when FXML is loaded
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // SETUP PREFERENCES
            preferences = Preferences.userNodeForPackage(LoginController.class);
            
            // SETUP DEPARTMENT BUTTONS
            setupDepartmentButtons();
            
            // SETUP PASSWORD TOGGLE
            setupPasswordToggle();
            
            // LOAD SAVED CREDENTIALS
            loadSavedCredentials();
            
            // SETUP FORM VALIDATION
            setupFormValidation();
            
            System.out.println("✅ Login Controller initialized successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing Login Controller: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * SETUP DEPARTMENT BUTTONS - Create department selection buttons
     */
    private void setupDepartmentButtons() {
        try {
            departmentGrid.getChildren().clear();
            
            for (int i = 0; i < DEPARTMENTS.length; i++) {
                Department dept = DEPARTMENTS[i];
                
                Button deptButton = new Button(dept.name);
                deptButton.getStyleClass().addAll("department-button");
                deptButton.setMaxWidth(Double.MAX_VALUE);
                deptButton.setOnAction(e -> selectDepartment(dept));
                
                // ADD TOOLTIP WITH FULL NAME
                Tooltip tooltip = new Tooltip(dept.fullName);
                Tooltip.install(deptButton, tooltip);
                
                // ADD TO GRID
                departmentGrid.add(deptButton, i % 3, i / 3);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error setting up department buttons: " + e.getMessage());
        }
    }
    
    /**
     * SELECT DEPARTMENT - Handle department button selection
     */
    private void selectDepartment(Department department) {
        try {
            // UPDATE SELECTED DEPARTMENT
            selectedDepartmentId = department.id;
            
            // UPDATE BUTTON STYLES
            updateDepartmentButtonStyles();
            
            // SHOW DEPARTMENT DESCRIPTION
            selectedDepartmentLabel.setText(department.fullName);
            selectedDepartmentLabel.setVisible(true);
            
            // CLEAR ERROR MESSAGE
            hideErrorMessage();
            
            System.out.println("🏫 Department selected: " + department.name);
            
        } catch (Exception e) {
            System.err.println("❌ Error selecting department: " + e.getMessage());
        }
    }
    
    /**
     * UPDATE DEPARTMENT BUTTON STYLES - Update visual state of department buttons
     */
    private void updateDepartmentButtonStyles() {
        departmentGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.getStyleClass().removeAll("department-button-selected");
                
                // CHECK IF THIS IS THE SELECTED DEPARTMENT
                for (Department dept : DEPARTMENTS) {
                    if (dept.name.equals(button.getText()) && dept.id.equals(selectedDepartmentId)) {
                        button.getStyleClass().add("department-button-selected");
                        break;
                    }
                }
            }
        });
    }
    
    /**
     * SETUP PASSWORD TOGGLE - Initialize password visibility toggle
     */
    private void setupPasswordToggle() {
        // SYNC PASSWORD FIELDS
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
        
        // UPDATE TOGGLE BUTTON
        updatePasswordToggleButton();
    }
    
    /**
     * TOGGLE PASSWORD VISIBILITY - Handle password show/hide button
     */
    @FXML
    private void togglePasswordVisibility() {
        try {
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
            
        } catch (Exception e) {
            System.err.println("❌ Error toggling password visibility: " + e.getMessage());
        }
    }
    
    /**
     * UPDATE PASSWORD TOGGLE BUTTON - Update toggle button appearance
     */
    private void updatePasswordToggleButton() {
        if (isPasswordVisible) {
            passwordToggleButton.setText("🙈");  // Hide password
            passwordToggleButton.setAccessibleText("Hide password");
        } else {
            passwordToggleButton.setText("👁");   // Show password
            passwordToggleButton.setAccessibleText("Show password");
        }
    }
    
    /**
     * LOAD SAVED CREDENTIALS - Load previously saved login information
     */
    private void loadSavedCredentials() {
        try {
            boolean wasRemembered = preferences.getBoolean(PREF_REMEMBER_ME, false);
            
            if (wasRemembered) {
                String savedUsername = preferences.get(PREF_USERNAME, "");
                String savedDepartment = preferences.get(PREF_DEPARTMENT, "");
                
                if (!savedUsername.isEmpty()) {
                    usernameField.setText(savedUsername);
                    rememberMeCheckbox.setSelected(true);
                    
                    // SELECT SAVED DEPARTMENT
                    if (!savedDepartment.isEmpty()) {
                        for (Department dept : DEPARTMENTS) {
                            if (dept.id.equals(savedDepartment)) {
                                selectDepartment(dept);
                                break;
                            }
                        }
                    }
                    
                    System.out.println("📋 Loaded saved credentials for: " + savedUsername);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error loading saved credentials: " + e.getMessage());
        }
    }
    
    /**
     * SETUP FORM VALIDATION - Setup real-time form validation
     */
    private void setupFormValidation() {
        // ENABLE/DISABLE SIGN IN BUTTON BASED ON FORM COMPLETION
        Runnable updateSignInButton = () -> {
            boolean isFormValid = !usernameField.getText().trim().isEmpty() &&
                                 !getCurrentPassword().trim().isEmpty() &&
                                 !selectedDepartmentId.isEmpty() &&
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
     * GET CURRENT PASSWORD - Get password from visible field
     */
    private String getCurrentPassword() {
        return isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
    }
    
    /**
     * HANDLE SIGN IN - Process login form submission
     */
    @FXML
    private void handleSignIn() {
        try {
            // VALIDATE FORM
            if (!validateForm()) {
                return;
            }
            
            // START LOADING STATE
            setLoadingState(true);
            
            // GET FORM DATA
            String username = usernameField.getText().trim();
            String password = getCurrentPassword();
            String department = selectedDepartmentId;
            boolean rememberMe = rememberMeCheckbox.isSelected();
            
            // CREATE BACKGROUND TASK FOR LOGIN
            Task<Boolean> loginTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    // SIMULATE API CALL - Replace with actual authentication
                    Thread.sleep(1500); // Simulate network delay
                    
                    // TODO: Replace with actual login validation
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
     * VALIDATE FORM - Check form inputs before submission
     */
    private boolean validateForm() {
        // CHECK DEPARTMENT SELECTION
        if (selectedDepartmentId.isEmpty()) {
            showErrorMessage("Please select your department.");
            return false;
        }
        
        // CHECK USERNAME
        if (usernameField.getText().trim().isEmpty()) {
            showErrorMessage("Please enter your username.");
            usernameField.requestFocus();
            return false;
        }
        
        // CHECK PASSWORD
        if (getCurrentPassword().trim().isEmpty()) {
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
     * PERFORM LOGIN - Actual login validation logic
     * TODO: Replace with actual authentication service
     */
    private boolean performLogin(String username, String password, String department) {
        // PLACEHOLDER IMPLEMENTATION
        // In a real application, this would call your authentication service
        
        System.out.println("🔐 Attempting login for: " + username + " (" + department + ")");
        
        // SIMPLE VALIDATION FOR DEMO (REMOVE IN PRODUCTION)
        return !username.isEmpty() && !password.isEmpty() && !department.isEmpty();
    }
    
    /**
     * HANDLE LOGIN SUCCESS - Process successful login
     */
    private void handleLoginSuccess(String username, String department, boolean rememberMe) {
        try {
            // HANDLE REMEMBER ME
            if (rememberMe) {
                preferences.put(PREF_USERNAME, username);
                preferences.put(PREF_DEPARTMENT, selectedDepartmentId);
                preferences.putBoolean(PREF_REMEMBER_ME, true);
                preferences.flush();
            } else {
                preferences.remove(PREF_USERNAME);
                preferences.remove(PREF_DEPARTMENT);
                preferences.putBoolean(PREF_REMEMBER_ME, false);
                preferences.flush();
            }
            
            // NOTIFY MAIN CONTROLLER
            if (mainController != null) {
                mainController.handleSuccessfulLogin(username, department);
            }
            
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
        try {
            // TODO: Implement forgot password functionality
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Forgot Password");
            alert.setHeaderText("Password Reset");
            alert.setContentText("Please contact the IT administrator to reset your password.\n\nEmail: it-support@rnscollege.edu\nPhone: +91-XXX-XXX-XXXX");
            alert.showAndWait();
            
            System.out.println("ℹ️ Forgot password link clicked");
            
        } catch (Exception e) {
            System.err.println("❌ Error handling forgot password: " + e.getMessage());
        }
    }
    
    /**
     * SHOW ERROR MESSAGE - Display error message to user
     */
    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setVisible(true);
    }
    
    /**
     * HIDE ERROR MESSAGE - Hide error message
     */
    private void hideErrorMessage() {
        errorMessageLabel.setVisible(false);
    }
    
    /**
     * SET LOADING STATE - Update UI for loading state
     */
    private void setLoadingState(boolean loading) {
        isLoading = loading;
        
        // UPDATE UI ELEMENTS
        signInButton.setText(loading ? SIGNING_IN_TEXT : SIGN_IN_TEXT);
        signInButton.setDisable(loading);
        loadingContainer.setVisible(loading);
        
        // DISABLE FORM FIELDS DURING LOADING
        usernameField.setDisable(loading);
        passwordField.setDisable(loading);
        passwordVisibleField.setDisable(loading);
        passwordToggleButton.setDisable(loading);
        rememberMeCheckbox.setDisable(loading);
        forgotPasswordButton.setDisable(loading);
        
        // DISABLE DEPARTMENT BUTTONS
        departmentGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setDisable(loading);
            }
        });
    }
    
    /**
     * SET MAIN CONTROLLER - Set reference to main controller
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}

/*
  CUSTOMIZATION GUIDE FOR NON-CODERS:
  
  1. CHANGING DEPARTMENTS:
     - Modify the DEPARTMENTS array at the top
     - Each department needs: id, name, and fullName
     - Grid will automatically adjust for new departments
  
  2. CHANGING FORM TEXT:
     - Modify the constants at the top (FORM_TITLE, etc.)
     - Text changes will apply throughout the form
  
  3. CHANGING VALIDATION RULES:
     - Modify validateForm() method
     - Add new checks as needed
     - Update error messages for clarity
  
  4. CHANGING LOGIN BEHAVIOR:
     - Modify performLogin() method
     - Replace with actual authentication service calls
     - Add proper error handling
  
  5. CHANGING REMEMBER ME BEHAVIOR:
     - Modify the PREF_* constants for different storage keys
     - Add more fields to save/restore as needed
  
  6. CHANGING UI FEEDBACK:
     - Modify setLoadingState() for different loading behavior
     - Change error/success message display methods
     - Customize button states and animations
*/