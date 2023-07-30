package telemetryconsole.com.example.Common.QueryFilters;

// Placeholder class only at this point
public class QueryFilter {

    private DateFilter dateFilter;
    private DeviceTypeFilter deviceTypeFilter;

    public DateFilter getDateFilter() {
        return dateFilter;
    }
    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }
    
    public DeviceTypeFilter getDeviceTypeFilter() {
        return deviceTypeFilter;
    }
    public void setDeviceTypeFilter(DeviceTypeFilter deviceTypeFilter) {
        this.deviceTypeFilter = deviceTypeFilter;
    }

    public QueryFilter() {}
}
