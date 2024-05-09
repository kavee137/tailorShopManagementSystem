package lk.ijse.tailorshopmanagementsystem.model.tm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTm {
    private int productID;
    private String productName;
    private String productColor;
    private String productSize;
    private Double unitPrice;
    private int qtyOnHand;
}


