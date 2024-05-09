package lk.ijse.tailorshopmanagementsystem.model;

import javafx.fxml.FXML;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Reservation {
    private int reservationID;
    private String customerID;
    private int paymentID;
    private Date reservationDate;
    private Date returnDate;
    private String status;
}

//
//
//
//reservationID   | int         | NO   | PRI | NULL       | auto_increment |
//        | customerID      | varchar(20) | YES  | MUL | NULL       |                |
//        | paymentID       | int         | YES  | MUL | NULL       |                |
//        | reservationDate | date        | YES  |     | NULL       |                |
//        | returnDate      | date        | YES  |     | NULL       |                |
//        | status          | varchar(20) | YES  |     | Incomplete |