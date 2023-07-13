package lk.ijse.wheeldeal.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
public class DriverTM {
    String DriverID;
    String Name;
    String Location;
    String Tel;
    String Availability;

    public DriverTM(String driverID, String name) {
        this.DriverID = driverID;
        this.Name = name;
    }
}
