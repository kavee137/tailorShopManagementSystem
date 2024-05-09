package lk.ijse.tailorshopmanagementsystem.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartTm {

    private String description;
    private String fabricName;
    private String fabricColor;
    private double fabricSize;
    private String measurements;
    private Double unitPrice;
    private int qty;
    private double total;
    private JFXButton btnRemove;

}
