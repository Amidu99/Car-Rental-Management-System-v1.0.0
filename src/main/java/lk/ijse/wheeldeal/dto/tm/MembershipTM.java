package lk.ijse.wheeldeal.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
public class MembershipTM {
    String Code;
    String Type;
    Double Discount;
    Double Fee;
}
