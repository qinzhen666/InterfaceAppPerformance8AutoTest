package getResource;

import commons.Commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GetMemory {

    private static double Memory = 0;

    public static double getMemoryUsageRate(String deviceName, String packagename){
        String memoryPercent = "0";
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
                        List<String> list = Arrays.asList(line.split("\\s+"));//以空格,回车,换行符为切片条件
                        if (list.get(6).contains("K")) {//对应Android6.0系统
                            memoryPercent = list.get(6).replace("%", "");
                        } else if (list.get(5).contains("K")) {
                            memoryPercent = list.get(4).replace("%", "");
                        } else if (list.get(4).contains("G") && list.get(5).contains("M") && list.get(6).contains("M")) {//对应Android8.0系统
                            memoryPercent = list.get(9);
                        }
                        break;
                    }
                }
                Memory = Double.parseDouble(memoryPercent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                proc.destroy();
            }
        } catch (IOException e) {
            System.out.println("Memory-请检查设备是否正确连接并打开调试");
            return -0.1;
        }
        return Commons.streamDouble(Memory);
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0;i<5;i++){//FKFBB19120151100 com.ss.android.ugc.aweme
            System.out.println("【Memory使用率是】"+getMemoryUsageRate("FKFBB19120151100", "com.ss.android."));
        }
    }
}
