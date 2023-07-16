package tests;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDateBody {
    private String checkin;
    private String checkout;

}
