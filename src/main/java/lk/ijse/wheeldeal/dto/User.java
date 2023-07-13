package lk.ijse.wheeldeal.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private String UserID;
    private String UserName;
    private String Password;
    private String PassHint;
    private String EmpID;
}
