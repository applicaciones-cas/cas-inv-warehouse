

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inv.warehouse.services.InvWarehouseModels;
import org.guanzon.cas.parameter.Bin;
import org.guanzon.cas.parameter.Color;
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
    static Model_Inv_Stock_Request_Master record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        record = new InvWarehouseModels(instance).InventoryStockRequestMaster();
    }

    @Test
    public void testNewRecord() {
        JSONObject loJSON;

        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }           
        
        loJSON = record.setBranchCode("M001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }  
        
        loJSON = record.setTransactionDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }     
        
        loJSON = record.setModifyingId(instance.getUserID());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }     
        
        loJSON = record.setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }     
        
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }  
    }
   
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
