package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class CustomerTm {
    private String customerID;
    private String NIC;
    private String customerName;
    private String customerAddress;
    private String customerTel;
    private String userID;
    private String status;
}