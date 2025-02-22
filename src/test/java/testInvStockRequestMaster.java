

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

//    @Test
//    public void testNewTransaction() {
//        String branchCd = instance.getBranchCode();
//        String industryId = "0001";
//        String categoryId = "0002";
//        String remarks = "this is a test.";
//        
//        String stockId = "M00125000001";
//        int quantity = 110;
//        String classify = "A";
//        int recOrder = 110;
//        int onHand = 10;
//        
//        JSONObject loJSON;
//        
//        loJSON = trans.InitTransaction();
//        if (!"success".equals((String) loJSON.get("result"))){
//            System.err.println((String) loJSON.get("message"));
//            Assert.fail();
//        } 
//
//        loJSON = trans.NewTransaction();
//        if (!"success".equals((String) loJSON.get("result"))){
//            System.err.println((String) loJSON.get("message"));
//            Assert.fail();
//        } 
//        
//        //you can use trans.searchBranch() when on UI 
//        trans.Master().setBranchCode(branchCd); //direct assignment of value
//        Assert.assertEquals(trans.Master().getBranchCode(), branchCd);
//        
//        //you can use trans.searchIndustry() when on UI 
//        trans.Master().setIndustryId(industryId); //direct assignment of value
//        Assert.assertEquals(trans.Master().getIndustryId(), industryId);
//        
//        //you can use trans.searchCategory() when on UI 
//        trans.Master().setCategoryId(categoryId); //direct assignment of value
//        Assert.assertEquals(trans.Master().getCategoryId(), categoryId);
//        
//        trans.Master().setRemarks(remarks);
//        Assert.assertEquals(trans.Master().getRemarks(), remarks);
//
//        trans.Detail(0).setStockId(stockId);
//        trans.Detail(0).setQuantity(quantity);
//        trans.Detail(0).setClassification(classify);
//        trans.Detail(0).setRecommendedOrder(recOrder);
//        trans.Detail(0).setQuantityOnHand(onHand);
//        
//        trans.AddDetail();
//        trans.Detail(1).setStockId("M00225000111");
//        trans.Detail(1).setQuantity(0);
//        trans.Detail(1).setClassification(classify);
//        trans.Detail(1).setRecommendedOrder(recOrder);
//        trans.Detail(1).setQuantityOnHand(onHand);
//        
//        trans.AddDetail();
//        trans.Detail(2).setStockId("M00225000222");
//        trans.Detail(2).setQuantity(10);
//        trans.Detail(2).setClassification(classify);
//        trans.Detail(2).setRecommendedOrder(recOrder);
//        trans.Detail(2).setQuantityOnHand(onHand);
//        
//        trans.AddDetail();
//        trans.Detail(3).setStockId("M00225000333");
//        trans.Detail(3).setQuantity(50);
//        trans.Detail(3).setClassification(classify);
//        trans.Detail(3).setRecommendedOrder(recOrder);
//        trans.Detail(3).setQuantityOnHand(onHand);
//        
//        trans.AddDetail();
//
//        loJSON = trans.SaveTransaction();
//        if (!"success".equals((String) loJSON.get("result"))) {
//            System.err.println((String) loJSON.get("message"));
//            Assert.fail();
//        }
//    }   
    
//    @Test
//    public void testOpenTransaction() {
//        JSONObject loJSON;
//        
//        loJSON = trans.InitTransaction();
//        if (!"success".equals((String) loJSON.get("result"))){
//            System.err.println((String) loJSON.get("message"));
//            Assert.fail();
//        } 
//
//        loJSON = trans.OpenTransaction("M00125000002");
//        if (!"success".equals((String) loJSON.get("result"))){
//            System.err.println((String) loJSON.get("message"));
//            Assert.fail();
//        } 
//        
//        //retreiving using column index
//        for (int lnCol = 1; lnCol <= trans.Master().getColumnCount(); lnCol++){
//            System.out.println(trans.Master().getColumn(lnCol) + " ->> " + trans.Master().getValue(lnCol));
//        }
//        //retreiving using field descriptions
//        System.out.println(trans.Master().Branch().getBranchName());
//        System.out.println(trans.Master().Category().getDescription());
//        
//        //retreiving using column index
//        for (int lnCtr = 0; lnCtr <= trans.Detail().size() - 1; lnCtr++){
//            for (int lnCol = 1; lnCol <= trans.Detail(lnCtr).getColumnCount(); lnCol++){
//                System.out.println(trans.Detail(lnCtr).getColumn(lnCol) + " ->> " + trans.Detail(lnCtr).getValue(lnCol));
//            }
//        }
//    }   
    
    @Test
    public void testUpdateTransaction() {
        JSONObject loJSON;
        
        loJSON = trans.InitTransaction();
        if (!"success".equals((String) loJSON.get("result"))){
            System.err.println((String) loJSON.get("message"));
            Assert.fail();
        } 

        loJSON = trans.OpenTransaction("M00125000002");
        if (!"success".equals((String) loJSON.get("result"))){
            System.err.println((String) loJSON.get("message"));
            Assert.fail();
        } 
        
        loJSON = trans.UpdateTransaction();
        if (!"success".equals((String) loJSON.get("result"))){
            System.err.println((String) loJSON.get("message"));
            Assert.fail();
        } 
        
        trans.Detail(1).setQuantity(0);
        trans.AddDetail();

        loJSON = trans.SaveTransaction();
        if (!"success".equals((String) loJSON.get("result"))) {
            System.err.println((String) loJSON.get("message"));
            Assert.fail();
        }
    }   
    
    @AfterClass
    public static void tearDownClass() {
        trans = null;
        instance = null;
    }
}
