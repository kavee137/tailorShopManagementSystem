package lk.ijse.tailorshopmanagementsystem.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class UserTm {
    private String userID;
    private String userName;
    private String userEmail;
    private String password;
    private String status;
}
