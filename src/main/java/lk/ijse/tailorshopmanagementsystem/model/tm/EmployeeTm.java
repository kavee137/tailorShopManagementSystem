package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeTm {
    private String employeeID;
    private String userID;
    private String NIC;
    private String position;
    private String employeeName;
    private String phoneNumber;
    private String employeeAddress;
    private String salary;
    private String status;

}
