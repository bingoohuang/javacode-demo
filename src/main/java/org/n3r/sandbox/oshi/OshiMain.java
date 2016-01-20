package org.n3r.sandbox.oshi;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.software.os.OperatingSystem;

public class OshiMain {
    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        Memory memory = hardware.getMemory();
        System.out.println(memory.getAvailable() / 1024. / 1024. / 1024.);
        System.out.println(memory.getTotal() / 1024. / 1024. / 1024.);

    }
}
