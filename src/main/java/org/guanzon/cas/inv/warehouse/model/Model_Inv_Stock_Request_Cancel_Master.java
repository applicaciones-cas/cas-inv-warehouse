package org.guanzon.cas.inv.warehouse.model;

//package org.guanzon.cas.inv.model;
//
//import java.sql.SQLException;
//import java.util.Date;
//import org.guanzon.appdriver.agent.services.Model;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.appdriver.constant.RecordStatus;
//import org.guanzon.cas.inv.services.InvModels;
//import org.guanzon.cas.parameter.model.Model_Branch;
//import org.guanzon.cas.parameter.model.Model_Inv_Location;
//import org.guanzon.cas.parameter.model.Model_Warehouse;
//import org.guanzon.cas.parameter.services.ParamModels;
//import org.json.simple.JSONObject;
//
//public class Model_Inv_Stock_Request_Cancel_Master extends Model{      
//    //reference objects
//    Model_Branch poBranch;
//    Model_Warehouse poWarehouse;
//    Model_Inventory poInventory;
//    Model_Inv_Location poLocation;
//    
//    @Override
//    public void initialize() {
//        try {
//            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
//            
//            poEntity.last();
//            poEntity.moveToInsertRow();
//
//            MiscUtil.initRowSet(poEntity);
//            
//            //assign default values
//            poEntity.updateObject("dStartEnc", "0000-00-00");
//            poEntity.updateObject("dTransact", "0000-00-00");
//            poEntity.updateString("cTranStat", RecordStatus.INACTIVE);
//            //end - assign default values
//
//            poEntity.insertRow();
//            poEntity.moveToCurrentRow();
//
//            poEntity.absolute(1);
//
//            ID = "sTransNox";
//            
//            //initialize reference objects
//            ParamModels model = new ParamModels(poGRider);
//            poBranch = model.Branch();
//            poWarehouse = model.Warehouse();
//            poLocation = model.InventoryLocation();
//            
//            poInventory = new InvModels(poGRider).Inventory();
//            //end - initialize reference objects
//            
//            pnEditMode = EditMode.UNKNOWN;
//        } catch (SQLException e) {
//            logwrapr.severe(e.getMessage());
//            System.exit(1);
//        }
//    }
//    
//    public JSONObject setTransNox(String transNox){
//        return setValue("sTransNox", transNox);
//    }
//    
//    public String getTransNox(){
//        return (String) getValue("sTransNox");
//    }
//    
//    public JSONObject setBranchCode(String branchCode){
//        return setValue("sBranchCd", branchCode);
//    }
//    
//    public String getBranchCode(){
//        return (String) getValue("sBranchCd");
//    }
//    
//    public JSONObject setCategoryID(String categoryId){
//        return setValue("sCategrCd", categoryId);
//    }
//    
//    public String getCategoryID(){
//        return (String) getValue("sCategrCd");
//    }
//    
//    public JSONObject setDateTransact(Date dateTransact){
//        return setValue("dTransact", dateTransact);
//    }
//    
//    public Date getDateTransact(){
//        return (Date) getValue("dTransact");
//    }
//    
//    public JSONObject setOrderNo(String orderNo){
//        return setValue("sOrderNox", orderNo);
//    }
//    
//    public String getOrderNo(){
//        return (String) getValue("sOrderNox");
//    }
//    
//    public JSONObject setRemarks(String remarks){
//        return setValue("sRemarksx", remarks);
//    }
//    
//    public String getRemarks(){
//        return (String) getValue("sRemarksx");
//    }
//
//    public JSONObject setApproved(String approved){
//        return setValue("sApproved", approved);
//    }
//    
//    public String getApproved(){
//        return (String) getValue("sApproved");
//    }
//    
//    public JSONObject setDateApproved(Date dateApproved){
//        return setValue("dApproved", dateApproved);
//    }
//    
//    public Date getDateApproved(){
//        return (Date) getValue("dApproved");
//    }
//    
//    public JSONObject setApprovalCode(String approvalCode){
//        return setValue("sAprvCode", approvalCode);
//    }
//    
//    public String getApprovalCode(){
//        return (String) getValue("sAprvCode");
//    }
//    
//    public JSONObject setEntryNo(int entryNo){
//        return setValue("nEntryNox", entryNo);
//    }
//    
//    public int getEntryNo(){
//        return (int) getValue("nEntryNox");
//    }   
//            
//    public JSONObject setStatus(String status){
//        return setValue("cTranStat", status);
//    }
//    
//    public String getStatus(){
//        return (String) getValue("cTranStat");
//    }
//    
//    public JSONObject setDateStartEnc(Date dateStartEnc){
//        return setValue("dStartEnc", dateStartEnc);
//    }
//    
//    public Date getDateStartEnc(){
//        return (Date) getValue("dStartEnc");
//    }
//    
//    public JSONObject setModifyingId(String modifyingId){
//        return setValue("sModified", modifyingId);
//    }
//    
//    public String getModifyingId(){
//        return (String) getValue("sModified");
//    }
//    
//    public JSONObject setModifiedDate(Date modifiedDate){
//        return setValue("dModified", modifiedDate);
//    }
//    
//    public Date getModifiedDate(){
//        return (Date) getValue("dModified");
//    }
//    
//    @Override
//    public String getNextCode() {
//        return "";
//    }
//    
//    @Override
//    public JSONObject openRecord(String Id1) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//    
//    //reference object models
//    public Model_Branch Branch() {
//        if (!"".equals((String) getValue("sBranchCd"))) {
//            if (poBranch.getEditMode() == EditMode.READY
//                    && poBranch.getBranchCode().equals((String) getValue("sBranchCd"))) {
//                return poBranch;
//            } else {
//                poJSON = poBranch.openRecord((String) getValue("sBranchCd"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poBranch;
//                } else {
//                    poBranch.initialize();
//                    return poBranch;
//                }
//            }
//        } else {
//            poBranch.initialize();
//            return poBranch;
//        }
//    }
//    
//    public Model_Warehouse Warehouse() {
//        if (!"".equals((String) getValue("sWHouseID"))) {
//            if (poWarehouse.getEditMode() == EditMode.READY
//                    && poWarehouse.getWarehouseId().equals((String) getValue("sWHouseID"))) {
//                return poWarehouse;
//            } else {
//                poJSON = poWarehouse.openRecord((String) getValue("sWHouseID"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poWarehouse;
//                } else {
//                    poWarehouse.initialize();
//                    return poWarehouse;
//                }
//            }
//        } else {
//            poWarehouse.initialize();
//            return poWarehouse;
//        }
//    }
//    
//    public Model_Inv_Location Location() {
//        if (!"".equals((String) getValue("sLocatnID"))) {
//            if (poLocation.getEditMode() == EditMode.READY
//                    && poLocation.getLocationId().equals((String) getValue("sLocatnID"))) {
//                return poLocation;
//            } else {
//                poJSON = poLocation.openRecord((String) getValue("sLocatnID"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poLocation;
//                } else {
//                    poLocation.initialize();
//                    return poLocation;
//                }
//            }
//        } else {
//            poLocation.initialize();
//            return poLocation;
//        }
//    }
//    
//    public Model_Inventory Inventory() {
//        if (!"".equals((String) getValue("sStockIDx"))) {
//            if (poInventory.getEditMode() == EditMode.READY
//                    && poInventory.getStockId().equals((String) getValue("sStockIDx"))) {
//                return poInventory;
//            } else {
//                poJSON = poInventory.openRecord((String) getValue("sStockIDx"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poInventory;
//                } else {
//                    poInventory.initialize();
//                    return poInventory;
//                }
//            }
//        } else {
//            poInventory.initialize();
//            return poInventory;
//        }
//    }
//}