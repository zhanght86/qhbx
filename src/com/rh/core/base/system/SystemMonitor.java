package com.rh.core.base.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.StringTokenizer;

import com.rh.core.base.Bean;
import com.rh.core.util.DateUtils;
import com.sun.management.OperatingSystemMXBean;

/**
 * 系统CPU和内存情况监控
 * @author Jerry Li
 *
 */
public class SystemMonitor {

    private static final int CPUTIME = 30;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private static String osVersion = null;

    private String mIpAddress = null;
    private float totalMemorySize = 0.0f;
    private float buffersMemory = 0.0f;
    private float cachedMemory = 0.0f;
    private float usedMemory = 0.0f;
    private float memoryRatio;

    /**
     * 获得当前的监控对象.
     * 
     * @return 返回构造好的监控对象
     */
    public Bean getMonitorInfo() {
        int kb = 1024;
        osVersion = System.getProperty("os.version");
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        // 操作系统
        String osName = System.getProperty("os.name");
        String osEnCoding = System.getProperty("sun.jnu.encoding");
        // 主机IP
        if (osName.toLowerCase().startsWith("windows")) {
            mIpAddress = this.getWindowsIp();
        } else {
            mIpAddress = this.getLinuxIP();
        }
        if (osName.toLowerCase().startsWith("windows")) {
            // 总的物理内存
            float totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize()
                    / kb;
            float usedPhysicalMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb
                    .getFreePhysicalMemorySize())
                    / kb;
            totalMemorySize = Float.parseFloat(String.format("%.1f",
                    totalPhysicalMemorySize));
            // 已使用的物理内存
            usedMemory = Float.parseFloat(String.format("%.1f",
                    usedPhysicalMemorySize));
            // windows内存使用率
            memoryRatio = Float.parseFloat(String.format("%.1f",
                    (usedMemory / totalMemorySize) * 100));
        } else {
            float[] result = null;
            result = getLinuxMemInfo();

            totalMemorySize = Float
                    .parseFloat(String.format("%.1f", result[0]));
            buffersMemory = Float.parseFloat(String.format("%.1f", result[1]));
            cachedMemory = Float.parseFloat(String.format("%.1f", result[2]));
            usedMemory = totalMemorySize - result[3];
            // linux内存使用率
            memoryRatio = Float
                    .parseFloat(String
                            .format("%.1f",
                                    ((usedMemory - (cachedMemory + buffersMemory)) / totalMemorySize) * 100));
        }

        // 获得cpu频率
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows(osEnCoding);
        } else {
            cpuRatio = this.getCpuRateForLinux();
        }
        Bean infoBean = new Bean();
        infoBean.set("OS_NAME", osName).set("OS_ENCODING", osEnCoding)
                .set("OS_PROCESSORS", Runtime.getRuntime().availableProcessors())
                .set("CPU_RATIO", cpuRatio).set("IP_ADDRESS", mIpAddress)
                .set("DATE_TIME", DateUtils.getDatetime()).set("BUF_MEMORY", buffersMemory)
                .set("CACHE_MEMORY", cachedMemory).set("USED_MEMORY", usedMemory)
                .set("TOTAL_MEMORY", totalMemorySize).set("MEMORY_RATIO", memoryRatio);
        return infoBean;
    }

    /**
     * 获得Linux下IP地址.
     * 
     * @return 返回Linux下IP地址
     * @author yinliang
     */
    public String getLinuxIP() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
                    .getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address) {
                            continue;
                        }
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return ip;
    }

    /**
     * 获取windows下的IP地址
     * @return IP地址
     */
    public String getWindowsIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获得Linux下CPU使用率.
     * @return 返回Linux下cpu使用率
     */
    private double getCpuRateForLinux() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try {

            Process process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);

            if (osVersion.startsWith("2.4")) {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                String user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();

                user = user.substring(0, user.indexOf("%"));
                system = system.substring(0, system.indexOf("%"));
                nice = nice.substring(0, nice.indexOf("%"));

                float userUsage = new Float(user).floatValue();
                float systemUsage = new Float(system).floatValue();
                float niceUsage = new Float(nice).floatValue();

                return (userUsage + systemUsage + niceUsage) / 100;
            } else {
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String cpuUsage = tokenStat.nextToken();

                Float usage = new Float(cpuUsage.substring(0, cpuUsage
                        .indexOf("%")));

                return ((1 - usage.floatValue() / 100) * 100);
            }

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            freeResource(is, isr, brStat);
            return 1;
        } finally {
            freeResource(is, isr, brStat);
        }

    }

    /**
     * 施放资源
     * @param is    输入流
     * @param isr   输入流reader
     * @param br    带缓存的reader
     */
    private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {
        try {
            if (is != null) {
                is.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    /**
     * 获得windows下CPU使用率.
     * 
     * @param encoding 操作系统字符集
     * @return 返回windows下cpu使用率
     */
    private double getCpuRatioForWindows(String encoding) {
        try {
            mIpAddress = InetAddress.getLocalHost().getHostAddress();
            String procCmd = System.getenv("windir")
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd), encoding);
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd), encoding);
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime))
                        .doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 
     * 读取CPU信息。
     * 
     * @param proc  进程
     * @param encoding 操作系统字符集编码
     * @return cpu信息
     */
    private long[] readCpu(final Process proc, String encoding) {
        long[] retn = new long[2];
        String line = "";
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream(), encoding);
            LineNumberReader input = new LineNumberReader(ir);
            line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = substring(line, capidx, cmdidx - 1).trim();
                String cmd = substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idletime += Long.valueOf(substring(line, kmtidx, rocidx - 1).trim()).longValue();
                    idletime += Long.valueOf(substring(line, umtidx, wocidx - 1).trim()).longValue();
                    continue;
                }

                kneltime += Long.valueOf(substring(line, kmtidx, rocidx - 1).trim()).longValue();
                usertime += Long.valueOf(substring(line, umtidx, wocidx - 1).trim()).longValue();

            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            System.out.println(line);
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取Linux下内存信息
     * @return 内存信息
     */
    public float[] getLinuxMemInfo() {
        File file = new File("/proc/meminfo");
        float[] result = new float[4];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            String str = null;
            StringTokenizer token = null;
            while ((str = br.readLine()) != null) {
                token = new StringTokenizer(str);
                if (!token.hasMoreTokens()) {
                    continue;
                }
                str = token.nextToken();
                if (!token.hasMoreTokens()) {
                    continue;
                }
                if (str.equalsIgnoreCase("MemTotal:")) {
                    result[0] = Long.parseLong(token.nextToken());

                }
                if (str.equalsIgnoreCase("Buffers:")) {
                    result[1] = Long.parseLong(token.nextToken());

                }
                if (str.equalsIgnoreCase("Cached:")) {
                    result[2] = Long.parseLong(token.nextToken());

                }
                if (str.equalsIgnoreCase("MemFree:")) {
                    result[3] = Long.parseLong(token.nextToken());

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 截取bytes串
     * @param src 源字符串
     * @param start 开始
     * @param end 结束
     * @return 截取后结果
     */
    public static String substring(String src, int start, int end) { 
        byte[] b = src.getBytes();
        String out = ""; 
        for (int i = start; i <= end; i++) { 
            out += (char) b[i];
        }
        return out; 
    }
    
    /**
     * 测试方法.
     * 
     * @param args
     * @throws Exception
     * @author YINLIANG
     */
    public static void information() {
        Bean info = null;
        SystemMonitor service = new SystemMonitor();
        try {
            info = service.getMonitorInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("操作系统名字：" + info.getStr("OS_NAME"));
        System.out.println("cpu使用率:" + info.getStr("CPU_RATIO"));
        System.out.println("内存使用率:" + info.getStr("MEMORY_RATIO"));
        System.out.println("主机IP地址:" + info.getStr("IP_ADDRESS"));
        System.out.println("获取数据时间:" + info.getStr("DATE_TIME"));
        System.out.println("操作系统字符集编码:" + info.getStr("OS_ENCODING"));
        System.out.println("CPU核数:" + info.getStr("OS_PROCESSORS"));
    }

    /**
     * 测试方法
     * @param args 参数
     */
    public static void main(String[] args) {
        SystemMonitor.information();
    }
}
