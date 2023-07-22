package telemetryconsole.com.example.Common;

import java.util.Date;

public final class QueryValidator {
    
    public static boolean IsValidSerialNo(String serialNumber) {
        // Simple implementation - may be more to be added for this check
        return serialNumber != null && serialNumber.startsWith("S");
    }

    public static boolean IsValidDateRange(Date fromDate, Date toDate) {
        // Not yet implemented
        return true;
    }
}
