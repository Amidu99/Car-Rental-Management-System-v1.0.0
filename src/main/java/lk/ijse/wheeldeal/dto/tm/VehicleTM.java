package lk.ijse.wheeldeal.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
public class VehicleTM {
    String VehiNo;
    String VehiType;
    String Model;
    Double CostPerKM;
    String Availability;
}
