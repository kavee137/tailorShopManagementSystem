package lk.ijse.tailorshopmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Customer1{
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerTel;
    private String status;
}
