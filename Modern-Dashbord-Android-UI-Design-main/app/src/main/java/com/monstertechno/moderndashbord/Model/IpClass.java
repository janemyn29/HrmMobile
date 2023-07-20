package com.monstertechno.moderndashbord.Model;

import java.util.ArrayList;

public class IpClass {
    public String ipString;
    public String ipNumeric;
    public String ipType;
    public boolean isBehindProxy;
    public String device;
    public String os;
    public String userAgent;
    public String family;
    public String versionMajor;
    public String versionMinor;
    public String versionPatch;
    public boolean isSpider;
    public boolean isMobile;
    public String userAgentDisplay;
    public String userAgentRaw;

    public IpClass() {
    }

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
    }

    public String getIpNumeric() {
        return ipNumeric;
    }

    public void setIpNumeric(String ipNumeric) {
        this.ipNumeric = ipNumeric;
    }

    public String getIpType() {
        return ipType;
    }

    public void setIpType(String ipType) {
        this.ipType = ipType;
    }

    public boolean isBehindProxy() {
        return isBehindProxy;
    }

    public void setBehindProxy(boolean behindProxy) {
        isBehindProxy = behindProxy;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(String versionMajor) {
        this.versionMajor = versionMajor;
    }

    public String getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(String versionMinor) {
        this.versionMinor = versionMinor;
    }

    public String getVersionPatch() {
        return versionPatch;
    }

    public void setVersionPatch(String versionPatch) {
        this.versionPatch = versionPatch;
    }

    public boolean isSpider() {
        return isSpider;
    }

    public void setSpider(boolean spider) {
        isSpider = spider;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public String getUserAgentDisplay() {
        return userAgentDisplay;
    }

    public void setUserAgentDisplay(String userAgentDisplay) {
        this.userAgentDisplay = userAgentDisplay;
    }

    public String getUserAgentRaw() {
        return userAgentRaw;
    }

    public void setUserAgentRaw(String userAgentRaw) {
        this.userAgentRaw = userAgentRaw;
    }
}
