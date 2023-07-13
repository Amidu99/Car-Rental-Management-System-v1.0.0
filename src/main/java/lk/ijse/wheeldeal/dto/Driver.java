package lk.ijse.wheeldeal.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class Driver {
    private String DriverID;
    private String Name;
    private String Location;
    private String Tel;
    private String Availability;
}