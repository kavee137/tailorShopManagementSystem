module lk.ijse.tailorshopmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.datatransfer;
    requires com.jfoenix;
    requires jasperreports;


    opens lk.ijse.tailorshopmanagementsystem to javafx.fxml;
    exports lk.ijse.tailorshopmanagementsystem;
    exports lk.ijse.tailorshopmanagementsystem.controller;
    opens lk.ijse.tailorshopmanagementsystem.controller to javafx.fxml;
    opens lk.ijse.tailorshopmanagementsystem.model.tm to javafx.base;

}