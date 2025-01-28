package lk.ijse.groupchatapplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.groupchatapplication.ServerApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    public Button btnOK;

    public LoginController() {
    }

    @FXML
    private AnchorPane rootNode;

    @FXML
    private static TextField userName;

    public static String logUser = "";

    @FXML
    void btnSubmitOnAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) btnOK.getScene().getWindow();
//        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class
                .getResource("server-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CHAT APPLICATION");
        stage.setScene(scene);
        stage.show();
    }
    public static String getLogUser() {
//        return logUser = userName.getText();
        return logUser="Pamoda";
    }

}
