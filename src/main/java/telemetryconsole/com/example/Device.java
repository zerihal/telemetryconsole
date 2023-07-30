package telemetryconsole.com.example;

import java.util.Date;

import telemetryconsole.com.example.Common.DeviceStatus;

public class Device extends QueryItem {

    private String deviceIdentifier;
    private String deviceName;
    private String deviceTypeName;
    private DeviceStatus deviceStatus;

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }   

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Device(Date loggedDT, String deviceIdentifier, String deviceName, String deviceTypeName, DeviceStatus status) {
        setDateLogged(loggedDT);
        setDeviceIdentifier(deviceIdentifier);
        setDeviceName(deviceName);
        setDeviceTypeName(deviceTypeName);
        setDeviceStatus(status);
    }
    
    public Device() {}
}
