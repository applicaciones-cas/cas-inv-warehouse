package org.guanzon.cas.inv.warehouse;

import java.util.ArrayList;
import java.util.Iterator;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inv.warehouse.services.InvWarehouseModels;
import org.guanzon.cas.parameter.Branch;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;

public class StockRequest extends Transaction{
    private final String SOURCE_CODE = "InvR";
    
    public JSONObject InitTransaction(){        
        poMaster = new InvWarehouseModels(poGRider).InventoryStockRequestMaster();
        poDetail = new InvWarehouseModels(poGRider).InventoryStockRequestDetail();
        paDetail = new ArrayList<>();        
        
        return initialize();
    }
    
    public JSONObject NewTransaction(){
        return newTransaction();
    }
    
    public JSONObject SaveTransaction(){
        return saveTransaction();
    }
    
    public JSONObject OpenTransaction(String transactionNo){        
        return openTransaction(transactionNo);
    }
    
    public JSONObject UpdateTransaction(){
        return updateTransaction();
    }
    
    public JSONObject AddDetail(){
        if (Detail(getDetailCount() - 1).getStockId().isEmpty()) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "Last row has empty item.");
            return poJSON;
        }
        
        return addDetail();
    }
    
    /*Search Master References*/
    public JSONObject searchBranch(String value, boolean byCode){
        try {
            Branch object = new ParamControllers(poGRider, logwrapr).Branch();
            object.setRecordStatus("1");

            poJSON = object.searchRecord(value, byCode);
    
            if ("success".equals((String) poJSON.get("result"))){
                Master().setBranchCode(object.getModel().getBranchCode());
            }    
        } catch (ExceptionInInitializerError e) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "This procedure must not be called when there is no UI.");
        }
        
        return poJSON;
    }
    
    //TODO: add searching for Industry
    
    public JSONObject searchCategory(String value, boolean byCode){
        try {
            Category object = new ParamControllers(poGRider, logwrapr).Category();
            object.setRecordStatus("1");

            poJSON = object.searchRecord(value, byCode);
    
            if ("success".equals((String) poJSON.get("result"))){
                Master().setCategoryId(object.getModel().getCategoryId());
            }    
        } catch (ExceptionInInitializerError e) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "This procedure must not be called when there is no UI.");
        }
        
        return poJSON;
    }
    /*End - Search Master References*/
        
    @Override
    public String getSourceCode(){
        return SOURCE_CODE;
    }    
    
    @Override
    public Model_Inv_Stock_Request_Master Master() {
        return (Model_Inv_Stock_Request_Master) poMaster;
    }

    @Override
    public Model_Inv_Stock_Request_Detail Detail(int row) {
        return (Model_Inv_Stock_Request_Detail) paDetail.get(row);
    }    
        
    @Override
    public JSONObject willSave() {
        /*Put system validations and other assignments here*/
        poJSON = new JSONObject();
        
        //remove items with no stockid or quantity order
        Iterator<Model> detail = Detail().iterator();
        while (detail.hasNext()) {
            if ("".equals((String) detail.next().getValue("sStockIDx")) ||
                (int)detail.next().getValue("nQuantity") <= 0) {
                detail.remove();
            }
        }

        //assign other info on detail
        for (int lnCtr = 0; lnCtr <= getDetailCount() - 1; lnCtr ++){            
            Detail(lnCtr).setTransactionNo(Master().getTransactionNo());
            Detail(lnCtr).setEntryNumber(lnCtr + 1);
        }
        
        if (getDetailCount() == 1){
            //do not allow a single item detail with no quantity order
            if (Detail(0).getQuantity() == 0) {
                poJSON.put("result", "error");
                poJSON.put("message", "Your order has zero quantity.");
                return poJSON;
            }
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public boolean save() {
        /*Put saving business rules here*/
        return true;
    }
    
    @Override
    public JSONObject saveOthers() {
        /*Only modify this if there are other tables to modify except the master and detail tables*/
        poJSON = new JSONObject();
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public void saveComplete() {
        /*This procedure was called when saving was complete*/
        System.out.println("Transaction saved successfully.");
    }
    
    @Override
    public void initSQL(){
        SQL_BROWSE = "SELECT";
    }
}