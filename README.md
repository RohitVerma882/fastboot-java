# Fastboot Java

Android library for sending fastboot commands from an Android device to a device running fastboot.

***Only supports fastboot over USB On-The-Go (OTG) connections.***

Original-source: https://github.com/google/fastboot-mobile

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
        public void onFastbootDeviceAttached(@NonNull String deviceId) {
            
        }

        @Override
        public void onFastbootDeviceDetached(@NonNull String deviceId) {
            
        }

        @Override
        public void onFastbootDeviceConnected(@NonNull String deviceId, @NonNull FastbootDeviceContext deviceContext) {
            // Do some fastboot stuff...
            FastbootResponse response = deviceContext.sendCommand(FastbootCommand.getVar("current-slot"));
            String bootSlot = response.getData();
        }
    });

FastbootDeviceManager.connectToDevice(/* Serial Number */ "abc123");
```
