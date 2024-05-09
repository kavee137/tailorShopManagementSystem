package lk.ijse.tailorshopmanagementsystem.model;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Data

public class Customer { // customer representation

    private String customerID;

    private String NIC;

    private String customerName;

    private String customerAddress;

    private String customerTel;

    private String userID;

    private String status;

}


