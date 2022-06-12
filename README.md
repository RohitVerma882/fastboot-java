# Fastboot Java

Android library for sending fastboot commands from an Android device to a device running fastboot.

***Only supports fastboot over USB On-The-Go (OTG) connections.***

Original-Source: https://github.com/google/fastboot-mobile

## Download

Add to project's build.gradle
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Add to module-level build.gradle
```gradle
dependencies {
	implementation 'com.github.RohitVerma882:fastboot-java:main-f3792d2b19-1'
}
```

## Usage
### List Attached Fastboot Devices
```java
// Includes connected devices.
List<String> deviceIds = FastbootDeviceManager.getAttachedDeviceIds();
```

### List Connected Fastboot Devices
```java
List<String> deviceIds = FastbootDeviceManager.getConnectedDeviceIds();
```

### Connect to a Fastboot Device
```java
FastbootDeviceManager.addFastbootDeviceManagerListener(
    new FastbootDeviceManagerListener() {
        @Override
        public void onFastbootDeviceAttached(String deviceId) {
            
        }

        @Override
        public void onFastbootDeviceDetached(String deviceId) {
            
        }

        @Override
        public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
            // Do some fastboot stuff...
            FastbootResponse response = deviceContext.sendCommand(FastbootCommand.getVar("current-slot"));
            String bootSlot = response.getData();
        }
    });

FastbootDeviceManager.connectToDevice(/* Device ID */ "/dev/bus/usb/001/002");
```
