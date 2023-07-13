package lk.ijse.wheeldeal.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
public class UserTM {
    String UserID;
    String UserName;
    String Password;
    String PassHint;
    String EmpID;
}
