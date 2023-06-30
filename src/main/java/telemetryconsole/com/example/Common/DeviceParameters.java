package telemetryconsole.com.example.Common;

public class DeviceParameters extends QueryParameters {

    private String deviceIdentifier;

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public DeviceParameters(String deviceIdent) {
        setDeviceIdentifier(deviceIdent);
        ParameterCount = 1;
    }
}
