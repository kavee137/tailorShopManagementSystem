package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class OrderViewTm {
    private String orderID;
    private String paymentID;
    private String NIC;
    private Date orderDate;
    private Date returnDate;
    private String orderStatus;
    private String customerName;
    private String customerAddress;
    private String customerTel;
    private String employeeID;
    private String employeeName;
    private String fabricID;
    private String fabricName;
    private String fabricColor;
    private String description;
    private String measurements;
    private double fabricSize;
    private double unitPrice;
    private int qty;
    private double total;
    private String paymentType;
    private double paymentPrice;
}
