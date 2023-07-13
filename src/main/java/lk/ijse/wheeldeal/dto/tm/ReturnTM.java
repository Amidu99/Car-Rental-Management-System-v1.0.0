package lk.ijse.wheeldeal.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
public class ReturnTM {
    String ReturnNo;
    String RideNo;
    Double Distance;
    Double Cost;
    String ReDate;
}