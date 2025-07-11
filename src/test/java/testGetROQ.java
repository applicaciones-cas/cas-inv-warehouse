import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inv.warehouse.ROQ;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testGetROQ {
    static GRiderCAS instance;
    static ROQ trans;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        trans = new ROQ(instance, instance.getBranchCode(), "0003");
    }

    @Test
    public void testNewTransaction() {        
        JSONObject loJSON;
        
        try {
           loJSON = trans.LoadRecommendedOrder();
           
           if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
           }
           
            CachedRowSet loROQ =  trans.getRecommendations();
            
            loROQ.last();
            System.out.println("TOTAL WITH ROQ ITEMS: " + loROQ.getRow());
            
            loROQ.beforeFirst();
            
            while(loROQ.next()){
                String lsModelNme = loROQ.getString("sModelNme");
                double lnROQ = loROQ.getDouble("nRecOrder");
                
                System.out.println("Model: " + lsModelNme + ", ROQ: " + lnROQ);
            }
        } catch (SQLException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }   
    
    @AfterClass
    public static void tearDownClass() {
        trans = null;
        instance = null;
    }
}
