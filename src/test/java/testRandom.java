import java.util.Date;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.SQLUtil;

public class testRandom {
    public static void main(String [] args){
        Date date1 = SQLUtil.toDate("2025-07-11", SQLUtil.FORMAT_SHORT_DATE);
        Date date2 = SQLUtil.toDate("2025-07-15", SQLUtil.FORMAT_SHORT_DATE);
        
        double lnResult = (double) CommonUtils.dateDiff(date2, date1) / (double) 26;
        System.out.println(lnResult);
    }
}
