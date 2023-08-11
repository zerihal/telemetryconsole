package telemetryconsole.com.example.Common;

import java.util.Date;

import telemetryconsole.com.example.Util.StringHelper;

public final class QueryValidator {
    
    public static boolean isValidSerialNo(String serialNumber) {
        // Simple implementation - may be more to be added for this check
        return !StringHelper.isStringNullOrEmpty(serialNumber) && serialNumber.startsWith("S");
    }

    public static boolean isValidDateRange(Date fromDate, Date toDate) {
        // Not yet implemented
        return true;
    }
}
