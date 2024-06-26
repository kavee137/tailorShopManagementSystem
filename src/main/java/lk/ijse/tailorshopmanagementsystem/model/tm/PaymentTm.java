package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class PaymentTm {
    private int reservationID;
    private String orderID;
    private int paymentID;
    private String paymentType;
    private double price;

}
