module lk.ijse.groupchatapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.groupchatapplication to javafx.fxml;
    exports lk.ijse.groupchatapplication;
    exports lk.ijse.groupchatapplication.Controller;
    opens lk.ijse.groupchatapplication.Controller to javafx.fxml;
}