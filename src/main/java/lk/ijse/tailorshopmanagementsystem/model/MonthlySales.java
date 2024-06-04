package lk.ijse.tailorshopmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySales {
    private int orderMonth;
    private double totalSales;
}
