package lk.ijse.wheeldeal.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class Return {
    private String ReturnNo;
    private String RideNo;
    private Double Distance;
    private Double Cost;
    private String ReDate;
}
