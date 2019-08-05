package getResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GetFlow {

    //先获取pid
    public static String getPid(String deviceName,String packageName) throws InterruptedException {

        Process process = null;
        String pid ;
        try {
            process = Runtime.getRuntime().exec("adb -s " + deviceName + " shell ps | grep " + packageName);
            if (process.waitFor() != 0){
                System.out.println("exit value = " + process.exitValue());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            if ((line = reader.readLine()) != null){
                if (line.contains(packageName)){
                    String[] split = line.split("\\s+");
                    List<String> list = Arrays.asList(split);
//                    System.out.println("list=" + list);
                    pid = list.get(1);
                    return pid;
                }
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            process.destroy();
        }
        return null;
    }

    public static long getWifiFlow(String deviceName,String packageName) throws InterruptedException {
        String pid = getPid(deviceName,packageName);
        Process process = null;
        long totalFlow = -1;
        try {
            process = Runtime.getRuntime().exec("adb -s " + deviceName + " shell cat /proc/" + pid + "/net/dev");
            if (process.waitFor() != 0) {
                System.err.println("exit value = " + process.exitValue());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("wlan0:")) {
                    System.out.println("line:"+line);
                    String[] list = line.trim().split("\\s+");
                    System.out.println("list1:"+list[1]);
                    System.out.println("list1:"+list[9]);
                    long rcvMB = Long.parseLong(list[1]) / 1024 / 1024;
                    long sendMB = Long.parseLong(list[9]) / 1024 / 1024;
                    System.out.println("【wifi下流量数据统计】：下行：" + rcvMB + "MB" + " 上行：" + sendMB + "MB");
                    totalFlow = rcvMB + sendMB;
                    break;
                }
            }
        }catch(IOException e){
                e.printStackTrace();
        }finally {
            process.destroy();
        }
        return totalFlow;
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("getflow = " + getWifiFlow("FKFBB19120151100", "com.ss.android.ugc.aweme"));
    }
}
