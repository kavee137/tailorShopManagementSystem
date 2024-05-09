package lk.ijse.tailorshopmanagementsystem.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data   // getter setter and tostring
public class Orders {
    private String orderID;
    private String customerID;
    private int paymentID;
    private String employeeID;
    private String orderDate;
    private String measurements;
    private String description;
    private String returnDate;
    private String fabricSize;
}
