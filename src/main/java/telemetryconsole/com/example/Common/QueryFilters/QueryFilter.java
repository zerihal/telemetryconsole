package telemetryconsole.com.example.Common.QueryFilters;

// Placeholder class only at this point
public class QueryFilter {

    private DateFilter _dateFilter;
    private DeviceTypeFilter _DeviceTypeFilter;

    public DateFilter get_dateFilter() {
        return _dateFilter;
    }
    public void set_dateFilter(DateFilter _dateFilter) {
        this._dateFilter = _dateFilter;
    }
    
    public DeviceTypeFilter get_DeviceTypeFilter() {
        return _DeviceTypeFilter;
    }
    public void set_DeviceTypeFilter(DeviceTypeFilter _DeviceTypeFilter) {
        this._DeviceTypeFilter = _DeviceTypeFilter;
    }

    public QueryFilter() {}
}
