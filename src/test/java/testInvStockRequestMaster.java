

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inv.warehouse.StockRequest;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testInvStockRequestMaster {
    static GRider instance;
    static StockRequest trans;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        trans = new StockRequest();
        trans.setApplicationDriver(instance);
        trans.setBranchCode(instance.getBranchCode());
        trans.setVerifyEntryNo(true);
        trans.setWithParent(false);
    }

    @Test
    public void testNewTransaction() {
        String branchCd = instance.getBranchCode();
        String industryId = "0001";
        String categoryId = "0002";
        String remarks = "this is a test.";
        
        String stockId = "M00125000001";
        int quantity = 110;
        String classify = "A";
        int recOrder = 110;
        int onHand = 10;
        
        JSONObject loJSON;
        
        loJSON = trans.InitTransaction();
        if (!"success".equals((String) loJSON.get("result"))) Assert.fail();

        loJSON = trans.NewTransaction();
        if (!"success".equals((String) loJSON.get("result"))) Assert.fail();

        trans.Master().setBranchCode(branchCd);
        Assert.assertEquals(trans.Master().getBranchCode(), branchCd);
        
        trans.Master().setIndustryId(industryId);
        Assert.assertEquals(trans.Master().getIndustryId(), industryId);
        
        trans.Master().setCategoryId(categoryId);
        Assert.assertEquals(trans.Master().getCategoryId(), categoryId);
        
        trans.Master().setRemarks(remarks);
        Assert.assertEquals(trans.Master().getRemarks(), remarks);

        trans.Detail(0).setStockId(stockId);
        trans.Detail(0).setQuantity(quantity);
        trans.Detail(0).setClassification(classify);
        trans.Detail(0).setRecommendedOrder(recOrder);
        trans.Detail(0).setQuantityOnHand(onHand);
        
        trans.AddDetail();
        trans.Detail(1).setStockId("M00225000111");
        trans.Detail(1).setQuantity(0);
        trans.Detail(1).setClassification(classify);
        trans.Detail(1).setRecommendedOrder(recOrder);
        trans.Detail(1).setQuantityOnHand(onHand);
        
        trans.AddDetail();
        trans.AddDetail();
//        trans.Detail(1).setStockId(stockId);

        loJSON = trans.SaveTransaction();
        if (!"success".equals((String) loJSON.get("result"))) {
            System.err.println((String) loJSON.get("message"));
            Assert.fail();
        }
        
//        loJSON = trans.clear();
//        if (!"success".equals((String) loJSON.get("result"))) Assert.fail();
    }   
    
    @AfterClass
    public static void tearDownClass() {
        trans = null;
        instance = null;
    }
}
