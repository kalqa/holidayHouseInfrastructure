import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.domain.entity.Reservation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTester {

    @Test
    public void checkDateInRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientToDate = null;
        Date clientFromDate = null;
        try {
            clientFromDate = sdf.parse("08-08-2019");
            clientToDate = sdf.parse("10-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Integer> availableRoomsForPeriod = getAvailableRoomsForPeriod(clientFromDate, clientToDate);
        assertEquals(1, availableRoomsForPeriod.size());

        assertEquals(222, availableRoomsForPeriod.get(0));
    }

    private List<Integer> getAvailableRoomsForPeriod(Date fromDate, Date toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientToDate = null;
        Date clientFromDate = null;
        try {
            clientFromDate = sdf.parse("09-08-2019");
            clientToDate = sdf.parse("10-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<ReservationDTO> reservationDTOS = new ArrayList<>();
        reservationDTOS.add(ReservationDTO.builder()
                .withRoomNumber(123)
                .withFromDate(clientFromDate)
                .withToDate(clientToDate)
                .build());

        try {
            clientFromDate = sdf.parse("20-08-2019");
            clientToDate = sdf.parse("30-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        reservationDTOS.add(ReservationDTO.builder()
                .withRoomNumber(222)
                .withFromDate(clientFromDate)
                .withToDate(clientToDate)
                .build());

        return reservationDTOS.stream()
                .map(reservationDTO -> mapToReservation(reservationDTO))
                .filter(reservation -> !isReservationNotAvailable(reservation, fromDate, toDate))
                .map(Reservation::getRoomNumber)
                .collect(Collectors.toList());
    }

    private boolean isReservationNotAvailable(Reservation reservation, Date clientFromDate, Date clientToDate) {
        Date fromDate = reservation.getFromDate();
        Date toDate = reservation.getToDate();
        boolean isClientFromDateInRange = isDataInRange(clientFromDate, fromDate, toDate);
        boolean isClientToDateInRange = isDataInRange(clientToDate, fromDate, toDate);
        return (isClientFromDateInRange || isClientToDateInRange);
    }

    public Reservation mapToReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setFromDate(reservationDTO.getFromDate());
        reservation.setToDate(reservationDTO.getToDate());
        reservation.setRoomNumber(reservationDTO.getRoomNumber());
        reservation.setUserName(reservationDTO.getUserName());
        return reservation;
    }

    @Test
    public void check() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientToDate = null;
        Date clientFromDate = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            clientFromDate = sdf.parse("09-08-2019");
            clientToDate = sdf.parse("10-08-2019");
            fromDate = sdf.parse("05-08-2019");
            toDate = sdf.parse("15-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean isClientFromDateInRange = clientFromDate.after(fromDate) && clientFromDate.before(toDate);
        boolean isClientToDateInRange = clientToDate.after(fromDate) && clientToDate.before(toDate);
        assertTrue(isClientFromDateInRange || isClientToDateInRange);
    }

    @Test
    public void check2() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientToDate = null;
        Date clientFromDate = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            clientFromDate = sdf.parse("09-08-2019");
            clientToDate = sdf.parse("10-08-2019");
            fromDate = sdf.parse("05-08-2019");
            toDate = sdf.parse("15-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean a = isDataInRange(clientFromDate, fromDate, toDate);
        boolean b = isDataInRange(clientToDate, fromDate, toDate);
        assertTrue(a || b);
    }

    private boolean isDataInRange(Date clientFromDate, Date fromDate, Date toDate) {
        return !(clientFromDate.before(fromDate) || clientFromDate.after(toDate));
    }

    private boolean isDataInRangeStack(Date clientFromDate, Date fromDate, Date toDate) {
        return !(clientFromDate.before(fromDate) || clientFromDate.after(toDate));
    }

    @Test
    public void testEqual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientFromDate = null;
        Date fromDate = null;
        Date toDate = null;
        try {
            clientFromDate = sdf.parse("09-08-2019");
            fromDate = sdf.parse("09-08-2019");
            toDate = sdf.parse("11-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(isDataInRangeStack(clientFromDate, fromDate, toDate));
    }
}
