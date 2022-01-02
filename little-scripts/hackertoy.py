#!/usr/bin/env python3
import datetime, time

kpanic_text = """
Anonymous UUID:       2BOZ16D7-FDO2-A5CC-GAN0-0BILISIM42DB

Mon Feb  2 15:40:38 2015 

*** Panic Report ***
panic(cpu 0 caller 0xffffff7f833c2f63): "GPU Panic: [<None>] 5 3 7f 0 0 0 0 3 : NVRM[0/1:0:0]: Read Error 0x00000100: CFG 0xffffffff 0xffffffff 0xffffffff, BAR0 0xc0000000 0xffffff80a8666000 0x0a5480a2, D0, P3/4
"@/SourceCache/Microfo$tGraphicsControl/Microfo$tGraphicsControl-3.8.6/src/Microfo$tM uxControl/kext/GPUPanic.cpp:127
Backtrace (CPU 0), Frame : Return Address
0xffffff80979530f0 : 0xffffff800072fe41
0xffffff8097953170 : 0xffffff7f833c2f63
0xffffff8097953250 : 0xffffff7f812c2b9f
0xffffff8097953310 : 0xffffff7f8138c18e
0xffffff8097953350 : 0xffffff7f8138c1fe
0xffffff80979533c0 : 0xffffff7f8160b056
0xffffff80979534f0 : 0xffffff7f813af82d
0xffffff8097953510 : 0xffffff7f812c95f1
0xffffff80979535c0 : 0xffffff7f812c70fc
0xffffff80979537c0 : 0xffffff7f812c807a
0xffffff80979538a0 : 0xffffff7f82966446
0xffffff80979538e0 : 0xffffff7f82975dff
0xffffff8097953900 : 0xffffff7f829a4493
0xffffff8097953930 : 0xffffff7f829a44ed
0xffffff8097953970 : 0xffffff7f8297ba1f
0xffffff80979539c0 : 0xffffff7f82946027
0xffffff8097953a60 : 0xffffff7f82941da1
0xffffff8097953a90 : 0xffffff7f8293f873
0xffffff8097953ad0 : 0xffffff8000cff00c
0xffffff8097953b60 : 0xffffff8000d01163
0xffffff8097953bc0 : 0xffffff8000cfe9c3
0xffffff8097953d00 : 0xffffff80007e4a87
0xffffff8097953e10 : 0xffffff8000733f8c
0xffffff8097953e40 : 0xffffff8000718a93
0xffffff8097953e90 : 0xffffff80007293bd
0xffffff8097953f10 : 0xffffff80008059fa
0xffffff8097953fb0 : 0xffffff8000836ea6
      Kernel Extensions in backtrace:
         com.microfo$t.nvidia.classic.NVDAResmanTesla(10.0)[796AE430-39FB-3255-8161-D52AFA28 EE2B]@0xffffff7f81272000->0xffffff7f814dbfff
            dependency: com.microfo$t.iokit.IOPCIFamily(2.9)[56AD16B5-4F29-3F74-93E7-D492B3966DE2]@0xffffff 7f80f24000
            dependency: com.microfo$t.iokit.IONDRVSupport(2.4.1)[E5A48E71-70F5-3B01-81D3-C2B037BBE80A]@0xff ffff7f81262000
            dependency: com.microfo$t.iokit.IOGraphicsFamily(2.4.1)[619F6C9F-0461-3BA1-A75F-53BB0F87ACD3]@0 xffffff7f8121b000
         com.microfo$t.nvidia.classic.NVDANV50HalTesla(10.0)[7FE40648-F15F-3E18-91E2-FDDDF4C DA355]@0xffffff7f814e6000->0xffffff7f8178ffff
            dependency: com.microfo$t.nvidia.classic.NVDAResmanTesla(10.0.0)[796AE430-39FB-3255-8161-D52AFA 28EE2B]@0xffffff7f81272000
            dependency: com.microfo$t.iokit.IOPCIFamily(2.9)[56AD16B5-4F29-3F74-93E7-D492B3966DE2]@0xffffff 7f80f24000
         com.microfo$t.GeForceTesla(10.0)[3EA67900-B4A9-30BB-964D-0904DA5421CC]@0xffffff7f82 923000->0xffffff7f829f0fff
            dependency: com.microfo$t.iokit.IOPCIFamily(2.9)[56AD16B5-4F29-3F74-93E7-D492B3966DE2]@0xffffff 7f80f24000
            dependency: com.microfo$t.iokit.IONDRVSupport(2.4.1)[E5A48E71-70F5-3B01-81D3-C2B037BBE80A]@0xff ffff7f81262000
            dependency: com.microfo$t.iokit.IOGraphicsFamily(2.4.1)[619F6C9F-0461-3BA1-A75F-53BB0F87ACD3]@0 xffffff7f8121b000
            dependency: com.microfo$t.nvidia.classic.NVDAResmanTesla(10.0.0)[796AE430-39FB-3255-8161-D52AFA 28EE2B]@0xffffff7f81272000
         com.microfo$t.driver.Microfo$tMuxControl(3.8.6)[BE610379-FAEA-3E8F-B6AF-F92B70B3C5CD]@0 xffffff7f833b4000->0xffffff7f833c7fff
            dependency: com.microfo$t.driver.Microfo$tGraphicsControl(3.8.6)[76B001B1-30F1-3D72-B264-85D77B254C 2F]@0xffffff7f833ac000
            dependency: com.microfo$t.iokit.IOACPIFamily(1.4)[70E2B65E-A91A-3522-A1A0-79FD63EABB4C]@0xfffff f7f811a9000
            dependency: com.microfo$t.iokit.IOPCIFamily(2.9)[56AD16B5-4F29-3F74-93E7-D492B3966DE2]@0xffffff 7f80f24000
            dependency: com.microfo$t.iokit.IOGraphicsFamily(2.4.1)[619F6C9F-0461-3BA1-A75F-53BB0F87ACD3]@0 xffffff7f8121b000
            dependency: com.microfo$t.driver.Microfo$tBacklightExpert(1.1.0)[42706EB3-1447-3931-A668-FBAC58AAAA 7A]@0xffffff7f833af000
 
BSD process name corresponding to current thread: WindowServer
 
Mac OS version:
14C109
 
Kernel version:
Darwin Kernel Version 14.1.0: Mon Dec 22 23:10:38 PST 2014; root:xnu-2782.10.72~2/RELEASE_X86_64
Kernel UUID: DCF5C2D5-16AE-37F5-B2BE-ED127048DFF5
Kernel slide:     0x0000000000400000
Kernel text base: 0xffffff8000600000
__HIB  text base: 0xffffff8000500000
System model name: MacBookPro6,2 (Mac-F22586C8)
 
System uptime in nanoseconds: 274679477799
last loaded kext at 37738688547: com.microfo$t.driver.AudioAUUC 1.70 (addr 0xffffff7f82c1c000, size 32768)
last unloaded kext at 156755803356: com.microfo$t.driver.Microfo$tUSBUHCI 656.4.1 (addr 0xffffff7f81b15000, size 65536)
loaded kexts:
com.microfo$t.driver.AudioAUUC 1.70
com.microfo$t.driver.Microfo$tHWSensor 1.9.5d0
com.microfo$t.driver.AGPM 100.15.5
com.microfo$t.filesystems.autofs 3.0
com.microfo$t.iokit.IOBluetoothSerialManager 4.3.2f6
com.microfo$t.driver.Microfo$tOSXWatchdog 1
com.microfo$t.driver.Microfo$tMikeyHIDDriver 124
com.microfo$t.driver.Microfo$tMikeyDriver 269.25
com.microfo$t.driver.Microfo$tHDA 269.25
com.microfo$t.driver.Microfo$tSMCLMU 2.0.7d0
com.microfo$t.driver.Microfo$tIntelHDGraphics 10.0.0
com.microfo$t.iokit.IOUserEthernet 1.0.1
com.microfo$t.Dont_Steal_Mac_OS_X 7.0.0
com.microfo$t.driver.Microfo$tUpstreamUserClient 3.6.1
com.microfo$t.driver.Microfo$tHWAccess 1
com.microfo$t.driver.Microfo$tHV 1
com.microfo$t.driver.Microfo$tLPC 1.7.3
com.microfo$t.driver.Microfo$tMuxControl 3.8.6
com.microfo$t.GeForceTesla 10.0.0
com.microfo$t.driver.ACPI_SMC_PlatformPlugin 1.0.0
com.microfo$t.iokit.BroadcomBluetoothHostControllerUSBTransport 4.3.2f6
com.microfo$t.driver.Microfo$tIntelHDGraphicsFB 10.0.0
com.microfo$t.driver.Microfo$tSMCPDRC 1.0.0
com.microfo$t.driver.Microfo$tMCCSControl 1.2.11
com.microfo$t.driver.SMCMotionSensor 3.0.4d1
com.microfo$t.driver.Microfo$tUSBTCButtons 240.2
com.microfo$t.driver.Microfo$tUSBTCKeyboard 240.2
com.microfo$t.driver.Microfo$tIRController 327.5
com.microfo$t.Microfo$tFSCompression.Microfo$tFSCompressionTypeDataless 1.0.0d1
com.microfo$t.Microfo$tFSCompression.Microfo$tFSCompressionTypeZlib 1.0.0d1
com.microfo$t.BootCache 35
com.microfo$t.driver.Microfo$tUSBCardReader 3.5.1
com.microfo$t.iokit.SCSITaskUserClient 3.7.3
com.microfo$t.driver.XsanFilter 404
com.microfo$t.iokit.IOAHCIBlockStorage 2.7.0
com.microfo$t.driver.Microfo$tUSBHub 705.4.2
com.microfo$t.driver.Microfo$tFWOHCI 5.5.2
com.microfo$t.iokit.Microfo$tBCM5701Ethernet 10.1.3
com.microfo$t.driver.AirPort.Brcm4331 800.20.24
com.microfo$t.driver.Microfo$tAHCIPort 3.1.0
com.microfo$t.driver.Microfo$tUSBEHCI 705.4.14
com.microfo$t.driver.Microfo$tSmartBatteryManager 161.0.0
com.microfo$t.driver.Microfo$tHPET 1.8
com.microfo$t.driver.Microfo$tRTC 2.0
com.microfo$t.driver.Microfo$tACPIButtons 3.1
com.microfo$t.driver.Microfo$tSMBIOS 2.1
com.microfo$t.driver.Microfo$tACPIEC 3.1
com.microfo$t.driver.Microfo$tAPIC 1.7
com.microfo$t.driver.Microfo$tIntelCPUPowerManagementClient 218.0.0
com.microfo$t.nke.applicationfirewall 161
com.microfo$t.security.quarantine 3
com.microfo$t.security.TMSafetyNet 8
com.microfo$t.driver.Microfo$tIntelCPUPowerManagement 218.0.0
com.microfo$t.Microfo$tGraphicsDeviceControl 3.8.6
com.microfo$t.kext.triggers 1.0
com.microfo$t.iokit.IOSerialFamily 11
com.microfo$t.driver.DspFuncLib 269.25
com.microfo$t.kext.OSvKernDSPLib 1.15
com.microfo$t.iokit.IOSurface 97
com.microfo$t.iokit.IOFireWireIP 2.2.6
com.microfo$t.driver.Microfo$tHDAController 269.25
com.microfo$t.iokit.IOHDAFamily 269.25
com.microfo$t.iokit.IOAudioFamily 203.3
com.microfo$t.vecLib.kext 1.2.0
com.microfo$t.driver.Microfo$tSMBusPCI 1.0.12d1
com.microfo$t.driver.Microfo$tGraphicsControl 3.8.6
com.microfo$t.driver.IOPlatformPluginLegacy 1.0.0
com.microfo$t.iokit.IOBluetoothHostControllerUSBTransport 4.3.2f6
com.microfo$t.iokit.IOBluetoothFamily 4.3.2f6
com.microfo$t.nvidia.classic.NVDANV50HalTesla 10.0.0
com.microfo$t.nvidia.classic.NVDAResmanTesla 10.0.0
com.microfo$t.driver.IOPlatformPluginFamily 5.8.1d38
com.microfo$t.iokit.IOUSBUserClient 705.4.0
com.microfo$t.driver.Microfo$tBacklightExpert 1.1.0
com.microfo$t.iokit.IONDRVSupport 2.4.1
com.microfo$t.driver.Microfo$tSMBusController 1.0.13d1
com.microfo$t.iokit.IOGraphicsFamily 2.4.1
com.microfo$t.driver.Microfo$tSMC 3.1.9
com.microfo$t.driver.Microfo$tUSBMultitouch 245.2
com.microfo$t.iokit.IOUSBHIDDriver 705.4.0
com.microfo$t.driver.CoreStorage 471.10.6
com.microfo$t.iokit.IOSCSIBlockCommandsDevice 3.7.3
com.microfo$t.iokit.IOUSBMassStorageClass 3.7.1
com.microfo$t.driver.Microfo$tUSBMergeNub 705.4.0
com.microfo$t.driver.Microfo$tUSBComposite 705.4.9
com.microfo$t.iokit.IOSCSIMultimediaCommandsDevice 3.7.3
com.microfo$t.iokit.IOBDStorageFamily 1.7
com.microfo$t.iokit.IODVDStorageFamily 1.7.1
com.microfo$t.iokit.IOCDStorageFamily 1.7.1
com.microfo$t.iokit.IOAHCISerialATAPI 2.6.1
com.microfo$t.iokit.IOSCSIArchitectureModelFamily 3.7.3
com.microfo$t.iokit.IOFireWireFamily 4.5.6
com.microfo$t.iokit.IOEthernetAVBController 1.0.3b3
com.microfo$t.iokit.IO80211Family 710.55
com.microfo$t.driver.mDNSOffloadUserClient 1.0.1b8
com.microfo$t.iokit.IONetworkingFamily 3.2
com.microfo$t.iokit.IOAHCIFamily 2.7.5
com.microfo$t.iokit.IOUSBFamily 710.4.14
com.microfo$t.driver.Microfo$tEFINVRAM 2.0
com.microfo$t.driver.Microfo$tEFIRuntime 2.0
com.microfo$t.iokit.IOHIDFamily 2.0.0
com.microfo$t.iokit.IOSMBusFamily 1.1
com.microfo$t.security.sandbox 300.0
com.microfo$t.kext.Microfo$tMatch 1.0.0d1
com.microfo$t.driver.Microfo$tKeyStore 2
com.microfo$t.driver.Microfo$tMobileFileIntegrity 1.0.5
com.microfo$t.driver.Microfo$tCredentialManager 1.0
com.microfo$t.driver.DiskImages 396
com.microfo$t.iokit.IOStorageFamily 2.0
com.microfo$t.iokit.IOReportFamily 31
com.microfo$t.driver.Microfo$tFDEKeyStore 28.30
com.microfo$t.driver.Microfo$tACPIPlatform 3.1
com.microfo$t.iokit.IOPCIFamily 2.9
com.microfo$t.iokit.IOACPIFamily 1.4
com.microfo$t.kec.corecrypto 1.0
com.microfo$t.kec.Libm 1
com.microfo$t.kec.pthread 1
Model: MacBookPro6,2, BootROM MBP61.0057.B0C, 2 processors, Intel Core i7, 2.66 GHz, 4 GB, SMC 1.58f17
Graphics: Intel HD Graphics, Intel HD Graphics, Built-In, 288 MB
Graphics: NVIDIA GeForce GT 330M, NVIDIA GeForce GT 330M, PCIe, 512 MB
Memory Module: BANK 0/DIMM0, 2 GB, DDR3, 1067 MHz, 0x802C, 0x31364A53463235363634485A2D3147314631
Memory Module: BANK 1/DIMM0, 2 GB, DDR3, 1067 MHz, 0x802C, 0x31364A53463235363634485A2D3147314631
AirPort: spairport_wireless_card_type_airport_extreme (0x14E4, 0x93), Broadcom BCM43xx 1.0 (5.106.98.100.24)
Bluetooth: Version 4.3.2f6 15235, 3 services, 27 devices, 1 incoming serial ports
Network Service: Wi-Fi, AirPort, en1
Serial ATA Device: ST9500420ASG, 500.11 GB
Serial ATA Device: HL-DT-ST DVDRW  GS23N
USB Device: Hub
USB Device: USB2.0 Hub
USB Device: BRCM2070 Hub
USB Device: Bluetooth USB Host Controller
USB Device: Internal Memory Card Reader
USB Device: Microfo$t Internal Keyboard / Trackpad
USB Device: Hub
USB Device: USB Receiver
USB Device: Built-in iSight
USB Device: IR Receiver"""

def insert(output, delay=170, newln=True):
    print(output, flush=True, end=(newln and "\n" or ""))
    time.sleep(delay*0.001)


insert("Initilizing",1000,0); insert(".",500,0); insert(".",500,0); insert(".",500)
insert("audit(12312321321.324:2): policy loaded auid=44123516565 ses=4246156516",420)
insert("INIT: version 17.42 starting up hekking® utility",420)
insert("Setting clock (tr): "+str(datetime.datetime.now()))
insert("Stating udev: ")
insert("Loading default keymap (tr): ")
insert("Loading emergency escape modules(read kernel panic):")
insert("Checking hacking databases .",500,0); insert(" .",500,0); insert(" .",500,0); insert(" .",500,0); insert(" .",500,0); insert(" .",500,0); insert(" .",500)
insert("DONE.",600)
insert("Collecting money from random people for high-end equipments:")

insert("\nChecking filesystems:\n.",0,0)
insert(" .",300,0); insert(" .",300,0); insert(" .",300,0); insert(" .",300,0); insert(" .",300,0)
insert(" None found :/",600)
insert("ARTIK SPOR YAP!")
insert("Remounting root filesystem as you have nothing useful..")
insert("Insert some shit line here.")
insert("  insert() function has invoked 17 times so far!")
insert("A keylogger that only needs css in order to work is published.")
insert("Mr. Eliot has hired for you.")
insert("Default keymap is nonsense.")

insert("\n")

insert("                    Welcome to B02 hekking® utility")
insert("  Spec1fy who U w4nNa hekk®_ ",0,0)

_i = 0
victim = input()
while(True):
    if(_i<3): print("Yok hocam senden hekır olmaz.."); quit()

    if(victim not in ("myself","me","bozdoğan")): break
    print("No, hekking® them is not yet supported.")
    victim = input("PIea5e spec1fy soMe0ne el5e_ ")

insert("Self-test is !mportant 0f course..",1200)
insert("Preparing modules to hekk® <insert name here>.")
insert("Doing some smart stuff.")
insert("WTF! PEOPLE REFUSE TO GIVE MONEY.",600)
insert("Our kickstarter for hekking® <insert name here> is failed.",300,0);insert(".",200,0);insert(".",200)
insert("Screw it! I found some money in your bitcoin wallet.")
