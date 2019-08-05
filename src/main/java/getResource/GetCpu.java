package getResource;

import commons.Commons;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GetCpu {

    private static double Cpu = 0;

    public static double getCpuUsageRate(String deviceName, String packagename){
        String cpuPercent = "0";
        //取得Runtime类的实例化对象,执行本机命令
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("adb -s " + deviceName + " shell top -n 1| grep " + packagename);
            try {
                if (proc.waitFor() != 0) {//未返回0,执行失败
                    System.err.println("exit value = " + proc.exitValue());
                }
                BufferedReader readerIn = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = readerIn.readLine()) != null) {
                    System.out.println("line等于"+line);
                    if (line.contains(packagename)&&!line.contains("/")) {
                        String[] list = line.trim().split("\\s+");//以空格,回车,换行符为切片条件
                        if (list[2].contains("%")) {
                            cpuPercent = list[2].replace("%", "");
                        } else if (list[4].contains("%")) {
                            cpuPercent = list[4].replace("%", "");
                        } else if (list[4].contains("G") && list[5].contains("M") && list[6].contains("M")) {//对应Android8.0系统
                            cpuPercent = list[8];
                        }
                        break;
                    }
                }
                Cpu = Double.parseDouble(cpuPercent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                proc.destroy();
            }
        } catch (IOException e) {
            System.out.println("CPU-请检查设备是否正确连接并打开调试");
            return -0.1;
        }
        return Commons.streamDouble(Cpu);
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0;i<5;i++){//FKFBB19120151100 com.ss.android.ugc.aweme
            System.out.println("【CPU使用率是】"+getCpuUsageRate("FKFBB19120151100", "com.ss.android."));
        }
    }
}
