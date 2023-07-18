package il.cshaifasweng.HSTS.client;

import java.io.IOException;

import il.cshaifasweng.HSTS.entities.MsgToLogIn;
import il.cshaifasweng.HSTS.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomePageController {
	private EventBus eventBus;
	//private StudentListSubscriber subscriber;
	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private ImageView eye;

	private App app;

	@FXML
	void sendWarning(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			showAlert("Error", "Failed to send warning: " + e.getMessage());
		}
	}

    /*@FXML
	void ClientProfileLoad(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("clientHomePage.fxml"));
			AnchorPane newScene = loader.load();

			Stage currentStage = App.getStage();
			Scene scene = new Scene(newScene);
			currentStage.setTitle("Client Home Page");
			currentStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	private void showAlert(String title, String message) {
		// Display an alert dialog to the user
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Subscribe
	public void onLogInEvent(LogInEvent message) throws IOException {
		User UserToSend =message.getMessage().getUser();
		if(message.getMessage().getLogInFlag().equals("Successfully")){
			User user = message.getMessage().getUser();
			app.setUser(user);
			if(user.getRole().equals("student")){
				Platform.runLater(() -> {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentHomePage.fxml"));
					try {
						AnchorPane newScene = loader.load();
						Stage currentStage = App.getStage();
						Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
						currentStage.setTitle(user.getFullName() + " Home Page");
						currentStage.setScene(scene);
						StudentHomePageController controller = loader.getController();
						controller.setUser(user);
						currentStage.show();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			} else if (user.getRole().equals("teacher")) {
				Platform.runLater(() -> {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherHomePage.fxml"));
					try {
						AnchorPane newScene = loader.load();
						TeacherHomePageController controller = loader.getController();
						controller.setUser(UserToSend);
						controller.setTeacher(message.getMessage().getTeacher());
						controller.initializee();
						Stage currentStage = App.getStage();
						Scene scene = new Scene(newScene);  // Set the loaded AnchorPane as the root of the scene
						currentStage.setTitle(user.getFullName() + " Home Page");
						currentStage.setScene(scene);
						//TeacherHomePageController controller = loader.getController();
						//controller.setUser(user);
						currentStage.show();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			} else if (user.getRole().equals("manager")) { //  this ToDo
				System.out.print("\nmanager!!\n");
			}

		} else if (message.getMessage().getLogInFlag().equals("WrongPassword")){ // here i think we need to show alert!
			EventBus.getDefault().post(new ErrorMsgEvent("Wrong Password"));
		}
		else if(message.getMessage().getLogInFlag().equals("WrongUsername")){
			EventBus.getDefault().post(new ErrorMsgEvent("Wrong Username"));
		} else if(message.getMessage().getLogInFlag().equals("UserOnline")){
			EventBus.getDefault().post(new ErrorMsgEvent("User is Online!"));
		} else {
			System.out.print(message.getMessage().getLogInFlag());
		}

	}

	public void logginin(ActionEvent actionEvent) {// the username for entity is id_num
		//EventBus.getDefault().register(this);
		try {
			//String string= new String("#LogInAttempt");
			MsgToLogIn msg = new MsgToLogIn("#LogInAttempt",passwordField.getText(),usernameField.getText() ) ;

			//Thread.sleep(500)
			SimpleClient.getClient().sendToServer(msg);
		} catch (IOException e) {
			showAlert("Error", "Failed to Get Login message!" + e.getMessage());
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			throw new RuntimeException(e);
		}*/
	}

	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
	}


	public void setApp(App app) {
		this.app = app;
	}
}