package com.housoo.platform.core.util;

import org.hyperic.sigar.*;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 系统信息工具类
 *
 * @author housoo
 */
public class PlatSystemInfoUtil {
    /**
     * 获取内存情况
     *
     * @return
     */
    public static Map<String, Object> getPhysicalMemory() {
        Map<String, Object> menoryMap = new HashMap<String, Object>();
        DecimalFormat df = new DecimalFormat("#0.00");
        Sigar sigar = new Sigar();
        Mem mem;
        try {
            mem = sigar.getMem();
            // 内存总量 单位G
            menoryMap.put("TotalMemory", df.format((float) mem.getTotal() / 1024 / 1024 / 1024));
            //当前内存使用量  单位G
            menoryMap.put("UsedMemory", df.format((float) mem.getUsed() / 1024 / 1024 / 1024));
            //当前已用占比
            menoryMap.put("UsedMemoryPre", df.format((float) mem.getUsed() * 100 / mem.getTotal()));
            //当前内存剩余量 单位G
            menoryMap.put("FreeMemory", df.format((float) mem.getFree() / 1024 / 1024 / 1024));
            // b)系统页面文件交换区信息 
            Swap swap = sigar.getSwap();
            //交换区总量 单位G
            menoryMap.put("TotalSwapped", df.format((float) swap.getTotal() / 1024 / 1024 / 1024));
            //当前交换区使用量 单位G
            menoryMap.put("UsedSwapped", df.format((float) swap.getUsed() / 1024 / 1024 / 1024));
            //当前交换区剩余量 单位G
            menoryMap.put("FreeSwapped", df.format((float) swap.getFree() / 1024 / 1024 / 1024));
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return menoryMap;
    }

    /**
     * 获取CPU个数
     *
     * @return
     */
    public static int getCpuCount() {
        int cpuCount = 0;
        Sigar sigar = new Sigar();
        try {
            cpuCount = sigar.getCpuInfoList().length;
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            sigar.close();
        }
        return cpuCount;
    }

    /**
     * 获取CPU大小等信息
     *
     * @return
     */
    public static List<Map<String, Object>> getCpuTotal() {
        List<Map<String, Object>> cpuList = new ArrayList<Map<String, Object>>();
        Sigar sigar = new Sigar();
        CpuInfo[] infos;
        DecimalFormat df = new DecimalFormat("#0.00");
        try {
            infos = sigar.getCpuInfoList();

            for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用 
                CpuInfo info = infos[i];
                Map<String, Object> cpuMap = new HashMap<String, Object>();
                // CPU的总量MHz 
                cpuMap.put("cpuMhz", df.format((float) info.getMhz() / 1000));
                // 获得CPU的卖主，如：Intel 
                cpuMap.put("cpuVendor", info.getVendor());
                // 获得CPU的类别，如：Celeron
                cpuMap.put("cpuModel", info.getModel());
                // 缓冲存储器数量
                cpuMap.put("cpuCacheSize", info.getCacheSize());
                //CPU逻辑个数
                cpuMap.put("cpuTotalCores", info.getTotalCores());
                if ((info.getTotalCores() != info.getTotalSockets()) ||
                        (info.getCoresPerSocket() > info.getTotalCores())) {
                    // CPU物理个数  
                    cpuMap.put("cpuTotalSockets", info.getTotalSockets());
                    //每个CPU核数
                    cpuMap.put("cpuCoresPerSocket", info.getCoresPerSocket());
                    //内核
                    cpuMap.put("cpuCoresPerSocketV",
                            info.getCoresPerSocket() / (info.getTotalSockets() + info.getTotalCores()));
                }
                cpuList.add(cpuMap);
            }
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return cpuList;
    }

    /**
     * 获取CPU使用率
     *
     * @return
     */
    public static List<Map<String, Object>> getCpuPerc() {
        List<Map<String, Object>> cpuPercResultList = new ArrayList<Map<String, Object>>();
        Sigar sigar = new Sigar();
        CpuPerc[] cpuPercList = null;
        try {
            cpuPercList = sigar.getCpuPercList();
            for (int i = 0; i < cpuPercList.length; i++) {
                Map<String, Object> cpuPercMap = new HashMap<String, Object>();
                // 用户使用率 
                cpuPercMap.put("cpuPercUser", CpuPerc.format(cpuPercList[i].getUser()));
                // 系统使用率 
                cpuPercMap.put("cpuPercSys", CpuPerc.format(cpuPercList[i].getSys()));
                // 当前等待率 
                cpuPercMap.put("cpuPercWait", CpuPerc.format(cpuPercList[i].getWait()));
                // 当前空闲率 
                cpuPercMap.put("cpuPercIdle", CpuPerc.format(cpuPercList[i].getIdle()));
                // 总的使用率 
                cpuPercMap.put("cpuPercCombined", CpuPerc.format(cpuPercList[i].getCombined()));
                cpuPercResultList.add(cpuPercMap);
            }
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return cpuPercResultList;
    }

    /**
     * @return
     */
    public static Map<String, Object> getEasyCpuPerc() {
        Map<String, Object> cpuPercMap = new HashMap<String, Object>();
        Sigar sigar = new Sigar();
        try {
            CpuPerc cpu = sigar.getCpuPerc();
            // 用户使用率 
            cpuPercMap.put("cpuPercUser", CpuPerc.format(cpu.getUser()));
            // 系统使用率 
            cpuPercMap.put("cpuPercSys", CpuPerc.format(cpu.getSys()));
            // 当前等待率 
            cpuPercMap.put("cpuPercWait", CpuPerc.format(cpu.getWait()));
            // 当前空闲率 
            cpuPercMap.put("cpuPercIdle", CpuPerc.format(cpu.getIdle()));
            // 总的使用率 
            cpuPercMap.put("cpuPercCombined", CpuPerc.format(cpu.getCombined()));
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return cpuPercMap;
    }

    /**
     * 取到当前计算机的名称
     *
     * @return
     */
    public static String getPlatformName() {
        String hostname = "";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception exc) {
            Sigar sigar = new Sigar();
            try {
                hostname = sigar.getNetInfo().getHostName();
            } catch (SigarException e) {
                hostname = "localhost.unknown";
            } finally {
                sigar.close();
            }
        }
        return hostname;
    }

    /**
     * 获取操作系统信息
     */
    public static Map<String, Object> getOSInfo() {
        Map<String, Object> osInfo = new HashMap<String, Object>();
        OperatingSystem OS = OperatingSystem.getInstance();
        // 操作系统内核类型如： 386、486、586等x86  操作系统的构架
        osInfo.put("osArch", OS.getArch());
        // 操作系统类型  操作系统的名称
        osInfo.put("osName", OS.getName());
        // 操作系统的版本号  操作系统的版本
        osInfo.put("osVersion", OS.getVersion());
        return osInfo;
    }

    /**
     * 获取java虚拟机允许的情况
     *
     * @return
     */
    public static Map<String, Object> getJVMInfo() {
        DecimalFormat df = new DecimalFormat("#0.00");
        Map<String, Object> jvmInfo = new HashMap<String, Object>();
        Runtime r = Runtime.getRuntime();
        //JVM可以使用的总内存 MB
        jvmInfo.put("totalMemory", df.format((float) r.totalMemory() / 1024 / 1024));
        //JVM已经使用的剩余内存 MB
        jvmInfo.put("usedMemory", df.format((float) (r.totalMemory() - r.freeMemory()) / 1024 / 1024));
        //JVM可以使用的剩余内存 MB
        jvmInfo.put("freeMemory", df.format((float) r.freeMemory() / 1024 / 1024));
        //JVM已经使用的剩余内存 %
        jvmInfo.put("usedMemoryPre", df.format((float) (r.totalMemory() - r.freeMemory()) * 100 / r.totalMemory()));
        //JVM可以使用的处理器个数
        jvmInfo.put("availableProcessors", r.availableProcessors());
        return jvmInfo;
    }

    /**
     * @throws Exception
     */
    public static List<Map<String, Object>> getFileInfo() {
        DecimalFormat df = new DecimalFormat("#0.00");
        List<Map<String, Object>> fileInfoList = new ArrayList<Map<String, Object>>();
        try {
            Sigar sigar = new Sigar();
            //通过sigar.getFileSystemList()来获得FileSystem列表对象，然后对其进行编历
            FileSystem[] fslist = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                Map<String, Object> fileInfo = new HashMap<String, Object>();
                FileSystem fs = fslist[i];
                // 分区的盘符名称
                fileInfo.put("devName", fs.getDevName());
                // 分区的盘符名称
                fileInfo.put("dieName", fs.getDirName());
                //盘符标志
                fileInfo.put("flags", fs.getFlags());
                // 文件系统类型,盘符类型，比如 FAT32、NTFS
                fileInfo.put("sysTypeName", fs.getSysTypeName());
                // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
                fileInfo.put("typeName", fs.getTypeName());
                // 文件系统类型
                fileInfo.put("type", fs.getType());
                FileSystemUsage usage = null;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小，单位 GB
                        fileInfo.put("usageTotal", df.format((float) usage.getTotal() / 1024 / 1024));
                        // 文件系统剩余大小，单位 GB
                        fileInfo.put("usageFree", df.format((float) usage.getFree() / 1024 / 1024));
                        // 文件系统可用大小，单位GB
                        fileInfo.put("usageAvail", df.format((float) usage.getAvail() / 1024 / 1024));
                        // 文件系统已经使用量，单位 KB
                        fileInfo.put("usageUsed", df.format((float) usage.getUsed() / 1024 / 1024));
                        double usePercent = usage.getUsePercent() * 100D;
                        // 文件系统资源的利用率
                        fileInfo.put("usageUsePercent", df.format(usePercent));
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
                //读出
                fileInfo.put("usageDiskReads", usage.getDiskReads());
                //写入
                fileInfo.put("usageDiskWrites", usage.getDiskWrites());
                fileInfoList.add(fileInfo);
            }
        } catch (SigarException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return fileInfoList;
    }
}
