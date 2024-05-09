package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SupplierTm {
    private String supplierID;
    private String NIC;
    private String supplierName;
    private String supplierAddress;
    private String supplierContact;
    private String status;
}
