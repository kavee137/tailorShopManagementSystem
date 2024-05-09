package lk.ijse.tailorshopmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
    private String productID;
    private String productName;
    private String productColor;
    private String productSize;
    private String unitPrice;
    private String qtyOnHand;
}
