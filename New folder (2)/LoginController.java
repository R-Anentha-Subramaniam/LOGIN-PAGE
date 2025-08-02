package yourpackage;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private RadioButton bcaRadio;
    @FXML private RadioButton bbaRadio;
    @FXML private RadioButton bcomRadio;
    @FXML private Button loginButton;
    private ToggleGroup departmentGroup;

    @FXML
    public void initialize() {
        departmentGroup = new ToggleGroup();
        bcaRadio.setToggleGroup(departmentGroup);
        bbaRadio.setToggleGroup(departmentGroup);
        bcomRadio.setToggleGroup(departmentGroup);
        bcaRadio.setSelected(true); // Default selection
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean remember = rememberMeCheckBox.isSelected();
        String department = ((RadioButton) departmentGroup.getSelectedToggle()).getText();

        // Replace this with your actual authentication logic
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Remember me: " + remember);
        System.out.println("Department: " + department);

        // Show alert for demonstration
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login attempted for " + department);
        alert.showAndWait();
    }
}
