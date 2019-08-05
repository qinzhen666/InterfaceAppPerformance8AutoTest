package commons;

public class Commons {

    public static double streamDouble(double UsageRate){
        //四舍五入
        double newUsageRate = (double) Math.round(UsageRate * 100) / 100;
        return newUsageRate;
    }
}
