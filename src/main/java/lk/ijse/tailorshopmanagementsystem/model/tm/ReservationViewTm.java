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

public class ReservationViewTm {
    private int reservationId;
    private String customerId;
    private String nic;
    private String customerName;
    private String customerAddress;
    private String telephone;
    private int paymentId;
    private Date reservationDate;
    private Date returnDate;
    private String status;
    private int productId;
    private int quantity;
    private double totalAmount;
    private String paymentType;
    private double paymentPrice;
}
