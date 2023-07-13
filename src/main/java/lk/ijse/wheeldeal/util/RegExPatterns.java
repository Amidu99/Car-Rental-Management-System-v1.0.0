package lk.ijse.wheeldeal.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class RegExPatterns {
    private static final Pattern namePattern = Pattern.compile("^[a-zA-Z '.-]{4,}$");
    private static final Pattern idPattern = Pattern.compile("^([0-9]{9}[x|X|v|V]|[0-9]{12})$");
    private static final Pattern telPattern = Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$");
    private static final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{5,}$");
    private static final Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9]{5,}$");
    private static final Pattern amountPattern = Pattern.compile("^(?:0|[1-9]\\d{0,4})(?:\\.\\d{1,2})?$");
    private static final Pattern customerIDPattern = Pattern.compile("^C0[0-9]{2}$");
    private static final Pattern employeeIDPattern = Pattern.compile("^E0[0-9]{2}$");
    private static final Pattern userIDPattern = Pattern.compile("^U0[0-9]{2}$");
    private static final Pattern vehicleNoPattern = Pattern.compile("^V0[0-9]{2}$");
    private static final Pattern rideNoPattern = Pattern.compile("^R0[0-9]{2}$");
    private static final Pattern returnNoPattern = Pattern.compile("^H0[0-9]{2}$");
    private static final Pattern driverIDPattern = Pattern.compile("^D0[0-9]{2}$");
    private static final Pattern membIDPattern = Pattern.compile("^M[0-9]$");
    private static final Pattern discountPattern = Pattern.compile("^(?!0\\d)(?:\\d|[1-9]\\d|99)(?:\\.\\d{1,2})?$");
    private static final Pattern distancePattern = Pattern.compile("^(?:0|[1-9]\\d{0,3})(?:\\.\\d{1,2})?$");
    private static final Pattern costKmPattern = Pattern.compile("^(?:0|[1-9]\\d{0,2})(?:\\.\\d{1,2})?$");
    private static final Pattern monthPattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
    private static final Pattern yearPattern = Pattern.compile("^(?:202\\d|20[3-9]\\d|2100)$");
    private static final Pattern vehiModelPattern = Pattern.compile("^(?=.[a-zA-Z])[a-zA-Z][a-zA-Z0-9\\s_-]{4,}$");

    public static Pattern getNamePattern(){return namePattern;}
    public static Pattern getIdPattern(){return idPattern;}
    public static Pattern getTelPattern(){return telPattern;}
    public static Pattern getUsernamePattern(){return usernamePattern;}
    public static Pattern getPasswordPattern(){return passwordPattern;}
    public static Pattern getAmountPattern(){return amountPattern;}
    public static Pattern getCustomerIDPattern(){return customerIDPattern;}
    public static Pattern getEmployeeIDPattern(){return employeeIDPattern;}
    public static Pattern getUserIDPattern(){return userIDPattern;}
    public static Pattern getVehicleNoPattern(){return vehicleNoPattern;}
    public static Pattern getRideNoPattern(){return rideNoPattern;}
    public static Pattern getReturnNoPattern(){return returnNoPattern;}
    public static Pattern getDriverIDPattern(){return driverIDPattern;}
    public static Pattern getMembIDPattern(){return membIDPattern;}
    public static Pattern getDiscountPattern(){return discountPattern;}
    public static Pattern getDistancePattern(){return distancePattern;}
    public static Pattern getCostKmPattern(){return costKmPattern;}
    public static Pattern getMonthPattern(){return monthPattern;}
    public static Pattern getYearPattern(){return yearPattern;}
    public static Pattern getVehiModelPattern(){return vehiModelPattern;}
    public static boolean datePattern(LocalDate date){
        return date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now());
    }
}