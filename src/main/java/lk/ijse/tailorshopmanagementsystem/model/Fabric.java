package lk.ijse.tailorshopmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class Fabric {

    private String fabricID;
    private String supplierID;
    private String fabricName;
    private String fabricColor;
    private int fabricQtyOnHand;

    public Fabric(String fabricID, String fabricName, String fabricColor, int fabricQtyOnHand) {
        this.fabricID = fabricID;
        this.fabricName = fabricName;
        this.fabricColor = fabricColor;
        this.fabricQtyOnHand = fabricQtyOnHand;
    }

}
