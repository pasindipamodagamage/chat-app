package lk.ijse.groupchatapplication.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    @FXML
    public Button cancel;
    @FXML
    private TextArea ServerTextArea;

    @FXML
    private TextField ServerTextField;

    @FXML
    void ServerSendBtnOnAction(ActionEvent event) {
        try {
            dataOutputStream.writeUTF(ServerTextField.getText().trim());
            dataOutputStream.flush();
            ServerTextArea.appendText("\n\nServer : "+ServerTextField.getText().trim());
            ServerTextField.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Socket socket;
    ServerSocket serverSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message="";

    String logUser=new LoginController().getLogUser();


    public void initialize(){

        new Thread(() -> {
            try {
                serverSocket=new ServerSocket(5000);
                ServerTextArea.appendText("Connecting ....... \n");
                socket=serverSocket.accept();
                ServerTextArea.appendText("\n"+logUser+" is Connected\n\n");

                dataInputStream=new DataInputStream(socket.getInputStream());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());

                while (!message.equals("Exit")){
                    message=dataInputStream.readUTF();
                    ServerTextArea.appendText("\n"+logUser+":    "+message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void ServerReceiveImageBtnOnAction(ActionEvent event) {
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

            ServerTextArea.appendText("\nServer: Sent an image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveImage() {
        try {
            int length = dataInputStream.readInt();
            byte[] buffer = new byte[length];
            dataInputStream.readFully(buffer);

            File receivedImage = new File("\nreceived_server_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(receivedImage);
            fileOutputStream.write(buffer);
            fileOutputStream.close();

            ServerTextArea.appendText("\n"+logUser+" Sent an image (saved as " + receivedImage.getAbsolutePath() + ")\n");
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
