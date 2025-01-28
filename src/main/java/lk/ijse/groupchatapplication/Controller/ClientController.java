package lk.ijse.groupchatapplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientController {
    @FXML
    public Button cancel;
    @FXML
    public Label userName;

    @FXML
    private TextArea ClientTextArea;

    @FXML
    private TextField ClientTextField;

    String message="";
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    String logUser=new LoginController().getLogUser();

    @FXML
    void ClientSendBtnOnAction(ActionEvent event) {
        try {
            dataOutputStream.writeUTF(ClientTextField.getText().trim());
            dataOutputStream.flush();
            ClientTextArea.appendText("\n\nServer : "+ClientTextField.getText().trim());
            ClientTextField.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(){
        new Thread(() ->{
            try {
                userName.setText(logUser+"'s Chat Box");

                socket=new Socket("localhost",5000);
                dataInputStream=new DataInputStream(socket.getInputStream());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());

                while (!message.equals("Exit")){
                    message=dataInputStream.readUTF();
                    ClientTextArea.appendText("\n\n"+logUser+" : "+message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    @FXML
    void ClientSendImageBtnOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            sendImage(file);
        }
    }

    private void sendImage(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();

            dataOutputStream.writeUTF("IMAGE");
            dataOutputStream.writeInt(buffer.length);
            dataOutputStream.write(buffer);
            dataOutputStream.flush();

            ClientTextArea.appendText("\n"+logUser+" : Sent an image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveImage() {
        try {
            int length = dataInputStream.readInt();
            byte[] buffer = new byte[length];
            dataInputStream.readFully(buffer);

            File receivedImage = new File("\n"+" received_client_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(receivedImage);
            fileOutputStream.write(buffer);
            fileOutputStream.close();

            ClientTextArea.appendText("\nServer: Sent an image (saved as " + receivedImage.getAbsolutePath() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnCancelOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
