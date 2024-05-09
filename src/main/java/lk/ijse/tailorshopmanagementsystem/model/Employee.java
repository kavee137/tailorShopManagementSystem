package lk.ijse.tailorshopmanagementsystem.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class Employee {
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
