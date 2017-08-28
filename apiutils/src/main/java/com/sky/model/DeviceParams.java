package com.sky.model;

/**
 * Created by SKY on 2017/8/25.
 */
public class DeviceParams {
    /**
     * devicesId : string
     * devicesSysType : string
     * devicesSysVersion : string
     * appVersion : string
     * appType : string
     * flag : string
     * flag3 : string
     * flag2 : string
     */

    private String devicesId;//设备ID
    private String devicesSysType = "2";//设备系统类型【1=公众号,2=安卓，3=IOS】
    private String devicesSysVersion;//设备系统版本
    private String appVersion;//应用版本
    private String appType;//应用类型【1:用户端 2:司机端】
    private String flag;
    private String flag3;
    private String flag2;

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }

    public void setDevicesSysType(String devicesSysType) {
        this.devicesSysType = devicesSysType;
    }

    public void setDevicesSysVersion(String devicesSysVersion) {
        this.devicesSysVersion = devicesSysVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setFlag3(String flag3) {
        this.flag3 = flag3;
    }

    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }
}
