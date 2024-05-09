package lk.ijse.tailorshopmanagementsystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class User {
    private String userID;
    private String userName;
    private String userEmail;
    private String password;
    private String status;
}
