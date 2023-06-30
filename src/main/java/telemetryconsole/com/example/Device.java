package telemetryconsole.com.example;

import java.util.Date;

import telemetryconsole.com.example.Common.DeviceStatus;

public class Device extends QueryItem {

    private String _deviceIdentifier;
    private String _deviceName;
    private String _deviceTypeName;
    private DeviceStatus _deviceStatus;

    public String get_deviceIdentifier() {
        return _deviceIdentifier;
    }

    public void set_deviceIdentifier(String _deviceIdentifier) {
        this._deviceIdentifier = _deviceIdentifier;
    }

    public String get_deviceName() {
        return _deviceName;
    }

    public void set_deviceName(String _deviceName) {
        this._deviceName = _deviceName;
    }

    public String get_deviceTypeName() {
        return _deviceTypeName;
    }

    public void set_deviceTypeName(String _deviceTypeName) {
        this._deviceTypeName = _deviceTypeName;
    }   

    public DeviceStatus get_deviceStatus() {
        return _deviceStatus;
    }

    public void set_deviceStatus(DeviceStatus _deviceStatus) {
        this._deviceStatus = _deviceStatus;
    }

    public Device(Date loggedDT, String deviceIdentifier, String deviceName, String deviceTypeName, DeviceStatus status) {
        set_dateLogged(loggedDT);
        set_deviceIdentifier(deviceIdentifier);
        set_deviceName(deviceName);
        set_deviceTypeName(deviceTypeName);
        set_deviceStatus(status);
    }
    
    public Device() {}
}
