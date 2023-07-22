package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StudentDetails{
    private User user;
    @FXML
    private TextField FirstNameField;

    @FXML
    private TextField UsernameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField EmaiAddresslField;

    @FXML
    private TextField PasswordField;

    @FXML
    private TextField LastNameField;

    // Other code and methods

    public void initializee() {
        // Initialize the text fields with user details
        FirstNameField.setText(user.getFirst());
        FirstNameField.setEditable(false);
        UsernameField.setText(user.getUsername());
        UsernameField.setEditable(false);
        idField.setText(user.getId());
        idField.setEditable(false);
        EmaiAddresslField.setText(user.getMail());
        EmaiAddresslField.setEditable(false);
        PasswordField.setText(user.getPassword());
        PasswordField.setEditable(false);
        LastNameField.setText(user.getLast());
        LastNameField.setEditable(false);
    }

    public void setUser(User user){this.user = user;}
}
