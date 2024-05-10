package lk.ijse.tailorshopmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {

    private String orderID;
    private String fabricID;
    private String description;
    private String measurements;
    private double fabricSize;
    private double unitPrice;
    private int qty;
    private double total;

}
