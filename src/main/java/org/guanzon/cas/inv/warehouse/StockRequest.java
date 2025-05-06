package org.guanzon.cas.inv.warehouse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.iface.GValidator;
import org.guanzon.cas.inv.Inventory;
import org.guanzon.cas.inv.services.InvControllers;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Master;
import org.guanzon.cas.inv.warehouse.services.InvWarehouseModels;
import org.guanzon.cas.inv.warehouse.status.StockRequestStatus;
import org.guanzon.cas.inv.warehouse.validators.StockRequestValidatorFactory;
import org.guanzon.cas.parameter.Branch;
import org.guanzon.cas.parameter.Brand;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.Industry;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class StockRequest extends Transaction{   
    public JSONObject InitTransaction(){      
        SOURCE_CODE = "InvR";
        
        poMaster = new InvWarehouseModels(poGRider).InventoryStockRequestMaster();
        poDetail = new InvWarehouseModels(poGRider).InventoryStockRequestDetail();
        paDetail = new ArrayList<>();        
        
        return initialize();
    }
    
    public JSONObject NewTransaction() throws CloneNotSupportedException{        
        return newTransaction();
    }
    
    public JSONObject SaveTransaction() throws SQLException, GuanzonException, CloneNotSupportedException{
        return saveTransaction();
    }
    
    public JSONObject OpenTransaction(String transactionNo) throws CloneNotSupportedException, SQLException, GuanzonException{        
        return openTransaction(transactionNo);
    }
    
    public JSONObject UpdateTransaction(){
        return updateTransaction();
    }
    
    public JSONObject ConfirmTransaction(String remarks) throws ParseException, SQLException, GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        
        String lsStatus = StockRequestStatus.CONFIRMED;
        boolean lbConfirm = true;
        
        if (getEditMode() != EditMode.READY){
            poJSON.put("result", "error");
            poJSON.put("message", "No transacton was loaded.");
            return poJSON;                
        }
        
        if (lsStatus.equals((String) poMaster.getValue("cTranStat"))){    
            poJSON.put("result", "error");
            poJSON.put("message", "Transaction was already confirmed.");
            return poJSON;                
        }
        
        //validator
        poJSON = isEntryOkay(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        //change status
        poJSON =  statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks,  lsStatus, !lbConfirm);
        
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        
        if (lbConfirm) poJSON.put("message", "Transaction confirmed successfully.");
        else poJSON.put("message", "Transaction confirmation request submitted successfully.");
        
        return poJSON;
    }
    
    public JSONObject PostTransaction(String remarks) throws ParseException, SQLException, GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        
        String lsStatus = StockRequestStatus.PROCESSED;
        boolean lbConfirm = true;
        
        if (getEditMode() != EditMode.READY){
            poJSON.put("result", "error");
            poJSON.put("message", "No transacton was loaded.");
            return poJSON;                
        }
        
        if (lsStatus.equals((String) poMaster.getValue("cTranStat"))){    
            poJSON.put("result", "error");
            poJSON.put("message", "Transaction was already processed.");
            return poJSON;                
        }
        
        //validator
        poJSON = isEntryOkay(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        //change status
        poJSON =  statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks,  lsStatus, !lbConfirm);
        
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        
        if (lbConfirm) poJSON.put("message", "Transaction posted successfully.");
        else poJSON.put("message", "Transaction posting request submitted successfully.");
        
        return poJSON;
    }
    
    public JSONObject CancelTransaction(String remarks) throws ParseException, SQLException, GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        
        String lsStatus = StockRequestStatus.CANCELLED;
        boolean lbConfirm = true;
        
        if (getEditMode() != EditMode.READY){
            poJSON.put("result", "error");
            poJSON.put("message", "No transacton was loaded.");
            return poJSON;                
        }
        
        if (lsStatus.equals((String) poMaster.getValue("cTranStat"))){    
            poJSON.put("result", "error");
            poJSON.put("message", "Transaction was already cancelled.");
            return poJSON;                
        }
        
        //validator
        poJSON = isEntryOkay(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        //change status
        poJSON =  statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks,  lsStatus, !lbConfirm);
        
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        
        if (lbConfirm) poJSON.put("message", "Transaction cancelled successfully.");
        else poJSON.put("message", "Transaction cancellation request submitted successfully.");
        
        return poJSON;
    }
    
    public JSONObject VoidTransaction(String remarks) throws ParseException, SQLException, GuanzonException, CloneNotSupportedException {
        poJSON = new JSONObject();
        
        String lsStatus = StockRequestStatus.VOID;
        boolean lbConfirm = true;
        
        if (getEditMode() != EditMode.READY){
            poJSON.put("result", "error");
            poJSON.put("message", "No transacton was loaded.");
            return poJSON;                
        }
        
        if (lsStatus.equals((String) poMaster.getValue("cTranStat"))){    
            poJSON.put("result", "error");
            poJSON.put("message", "Transaction was already voided.");
            return poJSON;                
        }
        
        //validator
        poJSON = isEntryOkay(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        //change status
        poJSON =  statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks,  lsStatus, !lbConfirm);
        
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        
        if (lbConfirm) poJSON.put("message", "Transaction voided successfully.");
        else poJSON.put("message", "Transaction voiding request submitted successfully.");
        
        return poJSON;
    }
    
    public JSONObject AddDetail() throws CloneNotSupportedException{
        if (Detail(getDetailCount() - 1).getStockId().isEmpty()) {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "Last row has empty item.");
            return poJSON;
        }
        
        return addDetail();
    }
    
    /*Search Master References*/
    public JSONObject SearchBranch(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException{
        Branch object = new ParamControllers(poGRider, logwrapr).Branch();
        object.setRecordStatus("1");

        poJSON = object.searchRecord(value, byCode);

        if ("success".equals((String) poJSON.get("result"))){
            Master().setBranchCode(object.getModel().getBranchCode());
        }    
        
        return poJSON;
    }
    
    public JSONObject SearchIndustry(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException{
        Industry object = new ParamControllers(poGRider, logwrapr).Industry();
        object.setRecordStatus("1");

        poJSON = object.searchRecord(value, byCode);

        if ("success".equals((String) poJSON.get("result"))){
            Master().setIndustryId(object.getModel().getIndustryId());
        }    
        
        return poJSON;
    }
    
    public JSONObject SearchCategory(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException{
        Category object = new ParamControllers(poGRider, logwrapr).Category();
        object.setRecordStatus("1");

        poJSON = object.searchRecord(value, byCode);

        if ("success".equals((String) poJSON.get("result"))){
            Master().setCategoryId(object.getModel().getCategoryId());
        }    
        
        return poJSON;
    }
    public JSONObject SearchBrand(String value, boolean byCode, int row) throws ExceptionInInitializerError, SQLException, GuanzonException {
        Brand brand = new ParamControllers(poGRider, logwrapr).Brand();
        brand.getModel().setRecordStatus(RecordStatus.ACTIVE);

        poJSON = brand.searchRecord(value, byCode, poGRider.getIndustry());

        if ("success".equals((String) poJSON.get("result"))) {
            Detail(row).setBrandId(brand.getModel().getBrandId());
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
    public JSONObject willSave() throws SQLException, GuanzonException{
        /*Put system validations and other assignments here*/
        poJSON = new JSONObject();
        
        //remove items with no stockid or quantity order       
        Iterator<Model> detail = Detail().iterator();
        while (detail.hasNext()) {
            Model item = detail.next(); // Store the item before checking conditions

            if ("".equals((String) item.getValue("sStockIDx"))
                    || (int) item.getValue("nQuantity") <= 0) {
                detail.remove(); // Correctly remove the item
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
    public JSONObject save() {
        /*Put saving business rules here*/
        return isEntryOkay(StockRequestStatus.OPEN);
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
    public JSONObject initFields() {
        /*Put initial model values here*/
        poJSON = new JSONObject();
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public void initSQL(){
        SQL_BROWSE = "";
    }
    
    @Override
    protected JSONObject isEntryOkay(String status){
        GValidator loValidator = StockRequestValidatorFactory.make(Master().getIndustryId());
        
        loValidator.setApplicationDriver(poGRider);
        loValidator.setTransactionStatus(status);
        
        poJSON = loValidator.validate();
        
        return poJSON;
    }
    public JSONObject isDetailHasZeroQty() {
        poJSON = new JSONObject();
        boolean allZeroQuantity = true;
        int tblRow = -1;

        for (int lnRow = 0; lnRow < getDetailCount() - 1; lnRow++) {
            int quantity = Detail(lnRow).getQuantity();
            String stockID = (String) Detail(lnRow).getValue("sStockIDx");

            if (!stockID.isEmpty()) {
                if (quantity != 0) {
                    allZeroQuantity = false;  // Found at least one item with non-zero quantity
                } else if (tblRow == -1) {
                    tblRow = lnRow;  // Capture the first row with zero quantity
                }
            }
        }

        if (allZeroQuantity) {
            poJSON.put("result", "success");
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "Some items have non-zero quantity. Do you want to proceed anyway?");
            poJSON.put("tableRow", tblRow);
        }

        return poJSON;
}

}