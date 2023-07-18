package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StudentDetails{
    private User user;
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField lastNameField;

    // Other code and methods

    public void initializee() {
        // Initialize the text fields with user details
        firstNameField.setText(user.getFirst());
        firstNameField.setEditable(false);
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);
        idField.setText(user.getId());
        idField.setEditable(false);
        mailField.setText(user.getMail());
        mailField.setEditable(false);
        passwordField.setText(user.getPassword());
        passwordField.setEditable(false);
        lastNameField.setText(user.getLast());
        lastNameField.setEditable(false);
    }

    public void setUser(User user){this.user = user;}
}
