package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.MsgToLogOut;
import il.cshaifasweng.HSTS.entities.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private SimpleClient client;
    private static Stage HomePageStage;
    private static User user;
    @Override
    public void start(Stage stage) throws IOException {
        HomePageStage = stage;
        EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("HomePage.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setTitle("High School Test System, Welcome!");
        stage.setScene(scene);
        HomePageController controller = fxmlLoader.getController();
        controller.setApp(this);
        stage.show();
    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    @Subscribe
    public void onErrorEvent(ErrorMsgEvent event) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

            Platform.runLater(() -> { // there is a possible that event can sent by another thread, here we ensure it sent by javafx thrad
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        String.format("Message: \nData: %s\nTimestamp: %s\n",
                                event.getErrorMsg(),
                                event.getTimeStamp().format(dtf))
                );
                alert.setTitle("Error!");
                alert.setHeaderText("Error:");
                alert.show();
            });

    }
    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
        MsgToLogOut msg = new MsgToLogOut("#LogOut", user) ;
        SimpleClient.getClient().sendToServer(msg);
        EventBus.getDefault().unregister(this);
        Platform.exit();
		super.stop();
	}
    public static Scene getScene(){
        return scene;
    }
    public static Stage getStage(){
        return HomePageStage;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public static void main(String[] args) {
        launch();
    }
}