/*
  SIGNUP CONTROLLER CLASS - Handles faculty registration form behavior
  
  INSTRUCTIONS FOR NON-CODERS:
  - This class controls the signup/registration form functionality
  - To change department options: Modify DEPARTMENTS array
  - To change designation options: Modify DESIGNATIONS array
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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * SIGNUP CONTROLLER CLASS
 * Handles the faculty registration form functionality including validation and account creation
 */
public class SignupController implements Initializable {
    
    // DEPARTMENT CONFIGURATION - CHANGEABLE: Add or remove departments here
    private static final Department[] DEPARTMENTS = {
        new Department("BCA", "BCA", "Bachelor of Computer Applications"),
        new Department("BBA", "BBA", "Bachelor of Business Administration"),
        new Department("BCOM", "BCOM", "Bachelor of Commerce")
    };
    
    // DESIGNATION CONFIGURATION - CHANGEABLE: Add or remove designations here
    private static final Designation[] DESIGNATIONS = {
        new Designation("professor", "Professor"),
        new Designation("associate_professor", "Associate Professor"),
        new Designation("assistant_professor", "Assistant Professor"),
        new Designation("lecturer", "Lecturer"),
        new Designation("visiting_faculty", "Visiting Faculty"),
        new Designation("guest_lecturer", "Guest Lecturer")
    };
    
    // VALIDATION PATTERNS
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[6-9]\\d{9}$"  // Indian mobile number format
    );
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{4,20}$"  // Alphanumeric with some special chars, 4-20 length
    );
    
    // UI TEXT CONSTANTS - CHANGEABLE: Modify these for different text
    private static final String FORM_TITLE = "Faculty Registration";
    private static final String FORM_DESCRIPTION = "Create your account to access the seminar hall booking system";
    private static final String CREATE_ACCOUNT_TEXT = "Create Account";
    private static final String CREATING_ACCOUNT_TEXT = "Creating Account...";
    private static final String BACK_TO_LOGIN_TEXT = "Back to Login";
    
    // FXML INJECTED FIELDS
    @FXML private VBox messageContainer;
    @FXML private Label successMessageLabel;
    @FXML private Label errorMessageLabel;
    
    // PERSONAL INFORMATION FIELDS
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dateOfBirthPicker;
    
    // PROFESSIONAL INFORMATION FIELDS
    @FXML private TextField facultyIdField;
    @FXML private TextField experienceField;
    @FXML private GridPane departmentGrid;
    @FXML private Label selectedDepartmentLabel;
    @FXML private ComboBox<String> designationComboBox;
    
    // ACCOUNT INFORMATION FIELDS
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button passwordToggleButton;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField confirmPasswordVisibleField;
    @FXML private Button confirmPasswordToggleButton;
    @FXML private CheckBox agreeToTermsCheckbox;
    
    // ACTION BUTTONS
    @FXML private Button createAccountButton;
    @FXML private Button backToLoginButton;
    @FXML private VBox loadingContainer;
    
    // INSTANCE VARIABLES
    private MainController mainController;
    private String selectedDepartmentId = "";
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private boolean isLoading = false;
    
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
     * DESIGNATION DATA CLASS
     */
    private static class Designation {
        final String id;
        final String name;
        
        Designation(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    
    /**
     * INITIALIZE METHOD - Called when FXML is loaded
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // SETUP DEPARTMENT BUTTONS
            setupDepartmentButtons();
            
            // SETUP DESIGNATION DROPDOWN
            setupDesignationComboBox();
            
            // SETUP PASSWORD TOGGLES
            setupPasswordToggles();
            
            // SETUP FORM VALIDATION
            setupFormValidation();
            
            // SETUP PHONE NUMBER FORMATTING
            setupPhoneNumberFormatting();
            
            System.out.println("✅ Signup Controller initialized successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing Signup Controller: " + e.getMessage());
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
     * SETUP DESIGNATION COMBO BOX - Populate designation dropdown
     */
    private void setupDesignationComboBox() {
        try {
            designationComboBox.getItems().clear();
            
            for (Designation designation : DESIGNATIONS) {
                designationComboBox.getItems().add(designation.name);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error setting up designation combo box: " + e.getMessage());
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
            hideMessages();
            
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
     * SETUP PASSWORD TOGGLES - Initialize password visibility toggles
     */
    private void setupPasswordToggles() {
        // SYNC PASSWORD FIELDS
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
        confirmPasswordVisibleField.textProperty().bindBidirectional(confirmPasswordField.textProperty());
        
        // UPDATE TOGGLE BUTTONS
        updatePasswordToggleButtons();
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
            
            updatePasswordToggleButtons();
            
        } catch (Exception e) {
            System.err.println("❌ Error toggling password visibility: " + e.getMessage());
        }
    }
    
    /**
     * TOGGLE CONFIRM PASSWORD VISIBILITY - Handle confirm password show/hide button
     */
    @FXML
    private void toggleConfirmPasswordVisibility() {
        try {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            
            confirmPasswordField.setVisible(!isConfirmPasswordVisible);
            confirmPasswordVisibleField.setVisible(isConfirmPasswordVisible);
            
            // FOCUS THE VISIBLE FIELD
            if (isConfirmPasswordVisible) {
                confirmPasswordVisibleField.requestFocus();
                confirmPasswordVisibleField.positionCaret(confirmPasswordVisibleField.getText().length());
            } else {
                confirmPasswordField.requestFocus();
                confirmPasswordField.positionCaret(confirmPasswordField.getText().length());
            }
            
            updatePasswordToggleButtons();
            
        } catch (Exception e) {
            System.err.println("❌ Error toggling confirm password visibility: " + e.getMessage());
        }
    }
    
    /**
     * UPDATE PASSWORD TOGGLE BUTTONS - Update toggle button appearance
     */
    private void updatePasswordToggleButtons() {
        // PASSWORD TOGGLE
        if (isPasswordVisible) {
            passwordToggleButton.setText("🙈");
            passwordToggleButton.setAccessibleText("Hide password");
        } else {
            passwordToggleButton.setText("👁");
            passwordToggleButton.setAccessibleText("Show password");
        }
        
        // CONFIRM PASSWORD TOGGLE
        if (isConfirmPasswordVisible) {
            confirmPasswordToggleButton.setText("🙈");
            confirmPasswordToggleButton.setAccessibleText("Hide confirm password");
        } else {
            confirmPasswordToggleButton.setText("👁");
            confirmPasswordToggleButton.setAccessibleText("Show confirm password");
        }
    }
    
    /**
     * SETUP PHONE NUMBER FORMATTING - Add phone number input validation
     */
    private void setupPhoneNumberFormatting() {
        // LIMIT TO DIGITS ONLY AND MAX 10 CHARACTERS
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 10) {
                phoneField.setText(newValue.substring(0, 10));
            }
        });
        
        // LIMIT EXPERIENCE TO DIGITS ONLY
        experienceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                experienceField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 2) {
                experienceField.setText(newValue.substring(0, 2));
            }
        });
    }
    
    /**
     * SETUP FORM VALIDATION - Setup real-time form validation
     */
    private void setupFormValidation() {
        // ENABLE/DISABLE CREATE ACCOUNT BUTTON BASED ON FORM COMPLETION
        Runnable updateCreateAccountButton = () -> {
            boolean isFormValid = isBasicFormValid() && !isLoading;
            createAccountButton.setDisable(!isFormValid);
        };
        
        // ADD LISTENERS TO REQUIRED FIELDS
        fullNameField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        experienceField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        designationComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        agreeToTermsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> updateCreateAccountButton.run());
        
        // INITIAL CHECK
        updateCreateAccountButton.run();
    }
    
    /**
     * CHECK IF BASIC FORM IS VALID - Quick validation for button state
     */
    private boolean isBasicFormValid() {
        return !fullNameField.getText().trim().isEmpty() &&
               !emailField.getText().trim().isEmpty() &&
               !phoneField.getText().trim().isEmpty() &&
               !selectedDepartmentId.isEmpty() &&
               designationComboBox.getValue() != null &&
               !experienceField.getText().trim().isEmpty() &&
               !usernameField.getText().trim().isEmpty() &&
               !getCurrentPassword().trim().isEmpty() &&
               !getCurrentConfirmPassword().trim().isEmpty() &&
               agreeToTermsCheckbox.isSelected();
    }
    
    /**
     * GET CURRENT PASSWORD - Get password from visible field
     */
    private String getCurrentPassword() {
        return isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
    }
    
    /**
     * GET CURRENT CONFIRM PASSWORD - Get confirm password from visible field
     */
    private String getCurrentConfirmPassword() {
        return isConfirmPasswordVisible ? confirmPasswordVisibleField.getText() : confirmPasswordField.getText();
    }
    
    /**
     * HANDLE CREATE ACCOUNT - Process registration form submission
     */
    @FXML
    private void handleCreateAccount() {
        try {
            // VALIDATE FORM
            if (!validateForm()) {
                return;
            }
            
            // START LOADING STATE
            setLoadingState(true);
            
            // GET FORM DATA
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            LocalDate dateOfBirth = dateOfBirthPicker.getValue();
            String facultyId = facultyIdField.getText().trim();
            String department = selectedDepartmentId;
            String designation = getSelectedDesignationId();
            String experience = experienceField.getText().trim();
            String username = usernameField.getText().trim();
            String password = getCurrentPassword();
            
            // CREATE BACKGROUND TASK FOR REGISTRATION
            Task<Boolean> registrationTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    // SIMULATE API CALL - Replace with actual registration
                    Thread.sleep(2000); // Simulate network delay
                    
                    // TODO: Replace with actual registration service
                    return performRegistration(fullName, email, phone, dateOfBirth, facultyId, 
                                             department, designation, experience, username, password);
                }
                
                @Override
                protected void succeeded() {
                    setLoadingState(false);
                    boolean registrationSuccess = getValue();
                    
                    if (registrationSuccess) {
                        handleRegistrationSuccess(username, email);
                    } else {
                        showErrorMessage("Registration failed. Please try again.");
                    }
                }
                
                @Override
                protected void failed() {
                    setLoadingState(false);
                    showErrorMessage("Registration failed. Please check your information and try again.");
                    System.err.println("❌ Registration task failed: " + getException().getMessage());
                }
            };
            
            // RUN REGISTRATION TASK
            new Thread(registrationTask).start();
            
        } catch (Exception e) {
            setLoadingState(false);
            showErrorMessage("An error occurred during registration. Please try again.");
            System.err.println("❌ Error handling create account: " + e.getMessage());
        }
    }
    
    /**
     * VALIDATE FORM - Comprehensive form validation
     */
    private boolean validateForm() {
        try {
            // VALIDATE FULL NAME
            if (fullNameField.getText().trim().isEmpty()) {
                showErrorMessage("Please enter your full name.");
                fullNameField.requestFocus();
                return false;
            }
            
            // VALIDATE EMAIL
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                showErrorMessage("Please enter your email address.");
                emailField.requestFocus();
                return false;
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                showErrorMessage("Please enter a valid email address.");
                emailField.requestFocus();
                return false;
            }
            
            // VALIDATE PHONE
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                showErrorMessage("Please enter your phone number.");
                phoneField.requestFocus();
                return false;
            }
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                showErrorMessage("Please enter a valid 10-digit phone number starting with 6-9.");
                phoneField.requestFocus();
                return false;
            }
            
            // VALIDATE DEPARTMENT
            if (selectedDepartmentId.isEmpty()) {
                showErrorMessage("Please select your department.");
                return false;
            }
            
            // VALIDATE DESIGNATION
            if (designationComboBox.getValue() == null) {
                showErrorMessage("Please select your designation.");
                designationComboBox.requestFocus();
                return false;
            }
            
            // VALIDATE EXPERIENCE
            String experience = experienceField.getText().trim();
            if (experience.isEmpty()) {
                showErrorMessage("Please enter your years of experience.");
                experienceField.requestFocus();
                return false;
            }
            
            // VALIDATE USERNAME
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showErrorMessage("Please enter a username.");
                usernameField.requestFocus();
                return false;
            }
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                showErrorMessage("Username must be 4-20 characters with letters, numbers, dots, underscores, or hyphens only.");
                usernameField.requestFocus();
                return false;
            }
            
            // VALIDATE PASSWORD
            String password = getCurrentPassword();
            if (password.isEmpty()) {
                showErrorMessage("Please enter a password.");
                if (isPasswordVisible) {
                    passwordVisibleField.requestFocus();
                } else {
                    passwordField.requestFocus();
                }
                return false;
            }
            if (password.length() < 8) {
                showErrorMessage("Password must be at least 8 characters long.");
                if (isPasswordVisible) {
                    passwordVisibleField.requestFocus();
                } else {
                    passwordField.requestFocus();
                }
                return false;
            }
            
            // VALIDATE PASSWORD CONFIRMATION
            String confirmPassword = getCurrentConfirmPassword();
            if (!password.equals(confirmPassword)) {
                showErrorMessage("Passwords do not match.");
                if (isConfirmPasswordVisible) {
                    confirmPasswordVisibleField.requestFocus();
                } else {
                    confirmPasswordField.requestFocus();
                }
                return false;
            }
            
            // VALIDATE TERMS ACCEPTANCE
            if (!agreeToTermsCheckbox.isSelected()) {
                showErrorMessage("Please accept the terms and conditions.");
                agreeToTermsCheckbox.requestFocus();
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error validating form: " + e.getMessage());
            showErrorMessage("Form validation error. Please check your inputs.");
            return false;
        }
    }
    
    /**
     * GET SELECTED DESIGNATION ID - Get the ID of selected designation
     */
    private String getSelectedDesignationId() {
        String selectedName = designationComboBox.getValue();
        if (selectedName != null) {
            for (Designation designation : DESIGNATIONS) {
                if (designation.name.equals(selectedName)) {
                    return designation.id;
                }
            }
        }
        return "";
    }
    
    /**
     * PERFORM REGISTRATION - Actual registration logic
     * TODO: Replace with actual registration service
     */
    private boolean performRegistration(String fullName, String email, String phone, LocalDate dateOfBirth,
                                      String facultyId, String department, String designation, String experience,
                                      String username, String password) {
        // PLACEHOLDER IMPLEMENTATION
        // In a real application, this would call your registration service
        
        System.out.println("📝 Attempting registration for: " + fullName + " (" + email + ")");
        System.out.println("   Department: " + department + ", Designation: " + designation);
        
        // SIMPLE VALIDATION FOR DEMO (REMOVE IN PRODUCTION)
        return !fullName.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty();
    }
    
    /**
     * HANDLE REGISTRATION SUCCESS - Process successful registration
     */
    private void handleRegistrationSuccess(String username, String email) {
        try {
            showSuccessMessage("Account created successfully! Please wait for admin approval.");
            
            // NOTIFY MAIN CONTROLLER
            if (mainController != null) {
                mainController.handleSuccessfulSignup(username, email);
            }
            
            System.out.println("✅ Registration successful for: " + username + " (" + email + ")");
            
        } catch (Exception e) {
            System.err.println("❌ Error handling registration success: " + e.getMessage());
        }
    }
    
    /**
     * HANDLE BACK TO LOGIN - Handle back to login button
     */
    @FXML
    private void handleBackToLogin() {
        try {
            if (mainController != null) {
                mainController.switchToView(MainController.View.LOGIN);
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling back to login: " + e.getMessage());
        }
    }
    
    /**
     * SHOW SUCCESS MESSAGE - Display success message to user
     */
    private void showSuccessMessage(String message) {
        hideMessages();
        successMessageLabel.setText(message);
        successMessageLabel.setVisible(true);
        messageContainer.setVisible(true);
    }
    
    /**
     * SHOW ERROR MESSAGE - Display error message to user
     */
    private void showErrorMessage(String message) {
        hideMessages();
        errorMessageLabel.setText(message);
        errorMessageLabel.setVisible(true);
        messageContainer.setVisible(true);
    }
    
    /**
     * HIDE MESSAGES - Hide all messages
     */
    private void hideMessages() {
        messageContainer.setVisible(false);
        successMessageLabel.setVisible(false);
        errorMessageLabel.setVisible(false);
    }
    
    /**
     * SET LOADING STATE - Update UI for loading state
     */
    private void setLoadingState(boolean loading) {
        isLoading = loading;
        
        // UPDATE BUTTONS
        createAccountButton.setText(loading ? CREATING_ACCOUNT_TEXT : CREATE_ACCOUNT_TEXT);
        createAccountButton.setDisable(loading);
        backToLoginButton.setDisable(loading);
        loadingContainer.setVisible(loading);
        
        // DISABLE ALL FORM FIELDS DURING LOADING
        fullNameField.setDisable(loading);
        emailField.setDisable(loading);
        phoneField.setDisable(loading);
        dateOfBirthPicker.setDisable(loading);
        facultyIdField.setDisable(loading);
        experienceField.setDisable(loading);
        designationComboBox.setDisable(loading);
        usernameField.setDisable(loading);
        passwordField.setDisable(loading);
        passwordVisibleField.setDisable(loading);
        passwordToggleButton.setDisable(loading);
        confirmPasswordField.setDisable(loading);
        confirmPasswordVisibleField.setDisable(loading);
        confirmPasswordToggleButton.setDisable(loading);
        agreeToTermsCheckbox.setDisable(loading);
        
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
  
  2. CHANGING DESIGNATIONS:
     - Modify the DESIGNATIONS array at the top
     - Each designation needs: id and name
  
  3. CHANGING VALIDATION RULES:
     - Modify validation patterns at the top
     - Update validateForm() method for custom rules
     - Change error messages for clarity
  
  4. CHANGING FORM BEHAVIOR:
     - Modify performRegistration() for actual service calls
     - Update handleRegistrationSuccess() for custom post-registration actions
     - Change form field requirements in isBasicFormValid()
  
  5. CHANGING UI TEXT:
     - Modify constants at the top for button text
     - Update error/success messages in validation methods
     - Change form labels in FXML file
  
  6. ADDING NEW FIELDS:
     - Add new FXML elements with fx:id
     - Add corresponding @FXML fields in controller
     - Update validation and data collection methods
     - Add to loading state disable/enable logic
*/