package org.guanzon.cas.inv.warehouse;

import com.google.protobuf.DoubleValue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.appdriver.iface.GValidator;
import org.guanzon.cas.inv.InvMaster;
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
    List<Model_Inv_Stock_Request_Master> paInvMaster;
    List<StockRequest> poStockRequest;
    static ROQ trans;
    public JSONObject InitTransaction(){      
        SOURCE_CODE = "InvR";
        
        poMaster = new InvWarehouseModels(poGRider).InventoryStockRequestMaster();
        poDetail = new InvWarehouseModels(poGRider).InventoryStockRequestDetail();
        paDetail = new ArrayList<>();        
        poStockRequest = new ArrayList<>();
      
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
    public Model_Inv_Stock_Request_Master INVMaster(int row) {
        return (Model_Inv_Stock_Request_Master) paInvMaster.get(row);
    }

    public int getINVMasterCount() {
        return this.paInvMaster.size();
    }
    private Model_Inv_Stock_Request_Master INVMasterList() {
        return new InvWarehouseModels(poGRider).InventoryStockRequestMaster();
    }
    private JSONObject saveUpdates(String status)
            throws CloneNotSupportedException {
        poJSON = new JSONObject();
        int lnCtr;
        
        try {
            for (lnCtr = 0; lnCtr <= poStockRequest.size() - 1; lnCtr++) {
                if (StockRequestStatus.CONFIRMED.equals(status)) {
                    poStockRequest.get(lnCtr).Master().setProcessed(true);
                }
                System.out.println("ts1" + poGRider.getUserID());
                System.out.println("ts2" + poGRider.getServerDate());
                poStockRequest.get(lnCtr).Master().setModifyingId(poGRider.getUserID());
                poStockRequest.get(lnCtr).Master().setModifiedDate(poGRider.getServerDate());
                poStockRequest.get(lnCtr).setWithParent(true);
                poJSON = poStockRequest.get(lnCtr).SaveTransaction();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.out.println("Stock Request Saving " + (String) poJSON.get("message"));
                    return poJSON;

                }
            }

        } catch (SQLException | GuanzonException ex) {
            Logger.getLogger(StockRequest.class
                    .getName()).log(Level.SEVERE, MiscUtil.getException(ex), ex);
            poJSON.put("result", "error");
            poJSON.put("message", MiscUtil.getException(ex));
            return poJSON;
        }
        poJSON.put("result", "success");
        return poJSON;
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
        if (!"success".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        if (poGRider.getUserLevel() <= UserRight.ENCODER) {
             poJSON = ShowDialogFX.getUserApproval(poGRider);
             if (!"success".equalsIgnoreCase((String) poJSON.get("result"))) {
                ShowMessageFX.Warning((String) poJSON.get("message"), null, null);
                return poJSON;
             }
        }
        poGRider.beginTrans("UPDATE STATUS", "Post Transaction", SOURCE_CODE, Master().getTransactionNo());

        poJSON = statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks, lsStatus, !lbConfirm, true);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }
        poJSON = saveUpdates(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }
        poGRider.commitTrans();
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        
        if (lbConfirm) {
        poJSON.put("message", "Transaction confirmed successfully.");
        }
        else {poJSON.put("message", "Transaction confirmation request submitted successfully.");}
        
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
        poGRider.beginTrans("UPDATE STATUS", "Post Transaction", SOURCE_CODE, Master().getTransactionNo());

        poJSON = statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks, lsStatus, !lbConfirm, true);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }
        poJSON = saveUpdates(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }

        poGRider.commitTrans();

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
        poGRider.beginTrans("UPDATE STATUS", "Post Transaction", SOURCE_CODE, Master().getTransactionNo());

        poJSON = statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks, lsStatus, !lbConfirm, true);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }
        poJSON = saveUpdates(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }

        poGRider.commitTrans();

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
        poGRider.beginTrans("UPDATE STATUS", "Post Transaction", SOURCE_CODE, Master().getTransactionNo());

        poJSON = statusChange(poMaster.getTable(), (String) poMaster.getValue("sTransNox"), remarks, lsStatus, !lbConfirm, true);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }
        poJSON = saveUpdates(StockRequestStatus.CONFIRMED);
        if (!"success".equals((String) poJSON.get("result"))) {
            poGRider.rollbackTrans();
            return poJSON;
        }

        poGRider.commitTrans();

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
    public JSONObject SearchBrand(String value, boolean byCode) throws ExceptionInInitializerError, SQLException, GuanzonException {
        Brand brand = new ParamControllers(poGRider, logwrapr).Brand();
        brand.getModel().setRecordStatus(RecordStatus.ACTIVE);
              
      
        poJSON = brand.searchRecord(value, byCode, Master().getIndustryId());
        
        if ("success".equals((String) poJSON.get("result"))) {
          poJSON.put("brandDesc", brand.getModel().getDescription());
          poJSON.put("brandID", brand.getModel().getBrandId());
        }

        return poJSON;
    }
    /*End - Search Master References*/
    
    
     /*Search Detail References*/
   public JSONObject SearchModel(String value, boolean byCode, String brandId, int row) throws SQLException, GuanzonException, NullPointerException {
 
    InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
    object.getModel().setRecordStatus(RecordStatus.ACTIVE);

        
          poJSON = object.Inventory().searchRecord(value, byCode, null, brandId, Master().getIndustryId(),Master().getCategoryId());
        
    if ("success".equals((String) poJSON.get("result"))) {
        for (int lnRow = 0; lnRow <= getDetailCount() - 1; lnRow++) {
            if (lnRow != row) {
                if ((Master().getSourceNo().equals("") || Master().getSourceNo() == null)
                        && (Detail(lnRow).getStockId().equals(object.Inventory().getModel().getStockId()))) {
                    
                    
                    double existingQty = Detail(lnRow).getQuantity();
                    double newQty = existingQty + 1;
                    Detail(lnRow).setQuantity(newQty);
                    
                   
                    Detail(row).setStockId("");  
                    poJSON.put("result", "merged");
                    poJSON.put("message", "Barcode already exists at row " + (lnRow + 1) + ", quantity updated.");
                    return poJSON;
                }
            }
        }
        
       Detail(row).setStockId(object.Inventory().getModel().getStockId());
        
     
    }

    return poJSON;
}

 public JSONObject SearchBarcode(String value, boolean byCode, int row, String brandId)
        throws ExceptionInInitializerError, SQLException, GuanzonException, CloneNotSupportedException, NullPointerException {

    InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
    object.setRecordStatus(RecordStatus.ACTIVE);

    poJSON = object.Inventory().searchRecord(value, byCode, null, brandId,
            Master().getIndustryId(), Master().getCategoryId());

    if ("success".equals((String) poJSON.get("result"))) {
        String stockId = object.Inventory().getModel().getStockId();

        for (int lnRow = 0; lnRow <= getDetailCount() - 1; lnRow++) {
            if (lnRow != row) {
                if ((Master().getSourceNo() == null || Master().getSourceNo().isEmpty())
                        && stockId.equals(Detail(lnRow).getStockId())) {

                    double existingQty = Detail(lnRow).getQuantity();
                    double newQty = existingQty + 1;
                    Detail(lnRow).setQuantity(newQty);

                    Detail(row).setStockId("");  

                    poJSON.put("result", "merged");
                    poJSON.put("message", "Barcode already exists at row " + (lnRow + 1) + ", quantity updated.");
                    return poJSON;
                }
            }
        }

        Detail(row).setStockId(stockId);

        poJSON.put("result", "success");
        poJSON.put("message", "Barcode added successfully.");
    }

    return poJSON;
}


 public JSONObject SearchBarcodeGeneral(String value, boolean byCode, int row, String brandId)
        throws ExceptionInInitializerError, SQLException, GuanzonException,
               CloneNotSupportedException, NullPointerException {

    InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
    object.setRecordStatus(RecordStatus.ACTIVE);

    poJSON = object.Inventory().searchRecord(value, byCode, null, brandId, null, Master().getCategoryId());

    if ("success".equals((String) poJSON.get("result"))) {
        String stockId = object.Inventory().getModel().getStockId();

        for (int lnRow = 0; lnRow <= getDetailCount() - 1; lnRow++) {
            if (lnRow != row) {
                if ((Master().getSourceNo() == null || Master().getSourceNo().isEmpty())
                        && stockId.equals(Detail(lnRow).getStockId())) {

                    double existingQty = Detail(lnRow).getQuantity();
                    double newQty = existingQty + 1;
                    Detail(lnRow).setQuantity(newQty);

                    Detail(row).setStockId("");  
                    poJSON.put("result", "merged");
                    poJSON.put("message", "Barcode already exists at row " + (lnRow + 1) + ", quantity updated.");
                    poJSON.put("updatedRow", lnRow);
                    return poJSON; 
                }
            }
        }

        Detail(row).setStockId(stockId);
        Detail(row).setCategoryCode(object.getModel().Inventory().getCategoryFirstLevelId());
        

        poJSON.put("result", "success");
        poJSON.put("message", "Barcode added successfully.");
    }

    return poJSON;
}

  public JSONObject SearchBarcodeDescription(String value, boolean byCode, int row, String brandId)
        throws ExceptionInInitializerError, SQLException, GuanzonException,
               CloneNotSupportedException, NullPointerException {

    InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
    object.setRecordStatus(RecordStatus.ACTIVE);

    poJSON = object.Inventory().searchRecord(value, byCode, null, brandId,
            Master().getIndustryId(), Master().getCategoryId());

    if ("success".equals((String) poJSON.get("result"))) {
        String stockId = object.Inventory().getModel().getStockId();

        for (int lnRow = 0; lnRow <= getDetailCount() - 1; lnRow++) {
            if (lnRow != row) {
                if ((Master().getSourceNo() == null || Master().getSourceNo().isEmpty())
                        && stockId.equals(Detail(lnRow).getStockId())) {

                    double existingQty = Detail(lnRow).getQuantity();
                    double newQty = existingQty + 1;
                    Detail(lnRow).setQuantity(newQty);

                    Detail(row).setStockId(""); 
                    poJSON.put("result", "merged");
                    poJSON.put("message", "Description already exists at row " + (lnRow + 1) + ", quantity updated.");
                    poJSON.put("updatedRow", lnRow); 
                    return poJSON;
                }
            }
        }

        Detail(row).setStockId(stockId);

        poJSON.put("result", "success");
        poJSON.put("message", "Description added successfully.");
    }

    return poJSON;
}

public JSONObject SearchBarcodeDescriptionGeneral(String value, boolean byCode, int row, String brandId) throws ExceptionInInitializerError, SQLException, GuanzonException, CloneNotSupportedException,
            NullPointerException {
        InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
    object.setRecordStatus(RecordStatus.ACTIVE);

    poJSON = object.Inventory().searchRecord(value, byCode, null, brandId,
            null, Master().getCategoryId());

    if ("success".equals((String) poJSON.get("result"))) {
        String stockId = object.Inventory().getModel().getStockId();

        for (int lnRow = 0; lnRow <= getDetailCount() - 1; lnRow++) {
            if (lnRow != row) {
                if ((Master().getSourceNo() == null || Master().getSourceNo().isEmpty())
                        && stockId.equals(Detail(lnRow).getStockId())) {

                    double existingQty = Detail(lnRow).getQuantity();
                    double newQty = existingQty + 1;
                    Detail(lnRow).setQuantity(newQty);

                    Detail(row).setStockId(""); 
                    poJSON.put("result", "merged");
                    poJSON.put("message", "Description already exists at row " + (lnRow + 1) + ", quantity updated.");
                    poJSON.put("updatedRow", lnRow); 
                    return poJSON;
                }
            }
        }

        Detail(row).setStockId(stockId);

        poJSON.put("result", "success");
        poJSON.put("message", "Description added successfully.");
    }

    return poJSON;
    }
    /*End - Search Detail References*/
       
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
                || ((Number) item.getValue("nQuantity")).doubleValue() <= 0) {
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
    
    @SuppressWarnings("unchecked")
    public List<Model_Inv_Stock_Request_Detail> getDetailList() {
        System.out.print("GET DETAIL LIST");
        System.out.println("paDetail = " + paDetail);
        System.out.println("paDetail class = " + (paDetail != null ? paDetail.getClass() : "null"));
        System.out.println("paDetail size = " + (paDetail != null ? paDetail.size() : "null"));

        return (List<Model_Inv_Stock_Request_Detail>) (List<?>) paDetail;
    }

    @Override
    protected JSONObject isEntryOkay(String status){
        System.out.println("IS ENTRY OK?");
        GValidator loValidator = StockRequestValidatorFactory.make(Master().getIndustryId());
        
        loValidator.setApplicationDriver(poGRider);
        loValidator.setTransactionStatus(status);
        loValidator.setMaster(poMaster);
        ArrayList laDetailList = new ArrayList<>(getDetailList());
        loValidator.setDetail(laDetailList);

        poJSON = loValidator.validate();
        
        if (poJSON.containsKey("isRequiredApproval") && Boolean.TRUE.equals(poJSON.get("isRequiredApproval"))) {
            if (poGRider.getUserLevel() <= UserRight.ENCODER) {
                poJSON = ShowDialogFX.getUserApproval(poGRider);
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
                } else {
                    if (Integer.parseInt(poJSON.get("nUserLevl").toString()) <= UserRight.ENCODER) {
                        poJSON.put("result", "error");
                        poJSON.put("message", "User is not an authorized approving officer.");
                        return poJSON;
                    }
                }
            }
        }
        
        return poJSON;
    }

  public JSONObject isDetailHasZeroQty() {
        poJSON = new JSONObject();
        int zeroQtyRow = -1;
        boolean hasNonZeroQty = false;
        boolean hasZeroQty = false;
        int lastRow = getDetailCount() - 1;

        for (int lnRow = 0; lnRow <= lastRow; lnRow++) {
            double quantity = Detail(lnRow).getQuantity();
            String stockID = (String) Detail(lnRow).getValue("sStockIDx");

            if (!stockID.isEmpty()) {
                if (quantity == 0.00) {
                    hasZeroQty = true;
                    if (zeroQtyRow == -1) {
                        zeroQtyRow = lnRow;
                    }
                } else {
                    hasNonZeroQty = true;
                }
            }
        }

        if (!hasNonZeroQty && hasZeroQty) {
            poJSON.put("result", "error");
            poJSON.put("message", "All items have zero quantity. Please enter a valid quantity.");
            poJSON.put("tableRow", zeroQtyRow);
            poJSON.put("warning", "true");
        } else if (hasZeroQty) {
            poJSON.put("result", "error");
            poJSON.put("message", "Some items have zero quantity. Please review.");
            poJSON.put("tableRow", zeroQtyRow);
            poJSON.put("warning", "false");
        } else {
            poJSON.put("result", "success");
            poJSON.put("message", "All items have valid quantities.");
            poJSON.put("tableRow", lastRow);
        }

        return poJSON;
    }

    
    
                  
        @Override
public void initSQL() {
    SQL_BROWSE = "SELECT " +
                 "  a.sTransNox, " +
                 "  a.dTransact, " +
                 "  a.sIndstCdx, " +     
                 "  a.sReferNox " +       
                 "FROM Inv_Stock_Request_Master a";
}


        public JSONObject searchTransaction(boolean searchRef) throws CloneNotSupportedException, SQLException, GuanzonException {
            poJSON = new JSONObject();
            String lsTransStat = "";

            if (psTranStat != null) {
                if (psTranStat.length() > 1) {
                    for (int lnCtr = 0; lnCtr <= psTranStat.length() - 1; lnCtr++) {
                        lsTransStat += ", " + SQLUtil.toSQL(Character.toString(psTranStat.charAt(lnCtr)));
                    }
                    lsTransStat = " AND a.cTranStat IN (" + lsTransStat.substring(2) + ")";
                } else {
                    lsTransStat = " AND a.cTranStat = " + SQLUtil.toSQL(psTranStat);
                }
            }

            initSQL();
            String lsSQL = MiscUtil.addCondition(SQL_BROWSE,
                    " a.sIndstCdx = " + SQLUtil.toSQL(Master().getIndustryId())
                  + " AND a.sCompnyID = " + SQLUtil.toSQL(Master().getCompanyID())
                  + " AND a.sCategrCd = " + SQLUtil.toSQL(Master().getCategoryId())
                  + " AND a.sBranchCD = " + SQLUtil.toSQL(poGRider.getBranchCode()));

          
            if (searchRef) {
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sReferNox IS NOT NULL AND a.sReferNox <> ''");
            }

            if (psTranStat != null && !"".equals(psTranStat)) {
                lsSQL = lsSQL + lsTransStat;
            }

            System.out.println("Executing SQL: " + lsSQL);

            poJSON = ShowDialogFX.Browse(poGRider,
                    lsSQL,
                    "",
                    "Transaction Date»Transaction No»Reference No",
                    "dTransact»sTransNox»sReferNox",
                    "a.dTransact»a.sTransNox»a.sReferNox",
                    1);

            if (poJSON != null) {
                return OpenTransaction((String) poJSON.get("sTransNox"));
            } else {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded.");
                return poJSON;
            }
        }

       public JSONObject searchTransaction()throws CloneNotSupportedException,SQLException,GuanzonException {
          
        poJSON = new JSONObject();
        String lsTransStat = "";
       
        if (psTranStat != null) {
            if (psTranStat.length() > 1) {
                for (int lnCtr = 0; lnCtr <= psTranStat.length() - 1; lnCtr++) {
                    lsTransStat += ", " + SQLUtil.toSQL(Character.toString(psTranStat.charAt(lnCtr)));
                }
               
                lsTransStat = " AND a.cTranStat IN (" + lsTransStat.substring(2) + ")";
            } else {
                lsTransStat = " AND a.cTranStat = " + SQLUtil.toSQL(psTranStat);
            }
        }
           
          
        initSQL();
        String lsSQL = MiscUtil.addCondition(SQL_BROWSE, " a.sIndstCdx = " + SQLUtil.toSQL(Master().getIndustryId())
               + " AND a.sCompnyID = " + SQLUtil.toSQL(Master().getCompanyID())
               + " AND a.sCategrCd = " + SQLUtil.toSQL(Master().getCategoryId())
               +" AND a.sBranchCD = " + SQLUtil.toSQL(poGRider.getBranchCode()));
//                + " AND a.sSupplier LIKE " + SQLUtil.toSQL("%" + Master().getSupplierId()));
        if (psTranStat != null && !"".equals(psTranStat)) {
            lsSQL = lsSQL + lsTransStat;
        }

        System.out.println("Executing SQL: " + lsSQL);
       
               poJSON = ShowDialogFX.Browse(poGRider,
                lsSQL,
                "",
                "Transaction Date»Transaction No»Reference No",   
                "dTransact»sTransNox»sReferNox",                  
                "a.dTransact»a.sTransNox»a.sReferNox",            
                1);



        if (poJSON != null) {
            return OpenTransaction((String) poJSON.get("sTransNox"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
       public JSONObject getTableListInformation(String fsTransNo, String fsReferID) throws SQLException, GuanzonException {
        JSONObject loJSON = new JSONObject();
        String lsTransStat = "";
        if (psTranStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psTranStat.length() - 1; lnCtr++) {
                lsTransStat += ", " + SQLUtil.toSQL(Character.toString(psTranStat.charAt(lnCtr)));
            }
            lsTransStat = " AND a.cTranStat IN (" + lsTransStat.substring(2) + ")";
        } else {
            lsTransStat = " AND a.cTranStat = " + SQLUtil.toSQL(psTranStat);
        }

        String lsSQL = "SELECT " +
                 "  a.sTransNox, " +
                 "  a.dTransact, " +
                 "  a.sIndstCdx, " +     
                 "  a.sReferNox " +       
                 "FROM Inv_Stock_Request_Master a";
        String lsFilterCondition = String.join(" AND ",
                " a.sIndstCdx = " + SQLUtil.toSQL(Master().getIndustryId()),
                " a.sCompnyID = " + SQLUtil.toSQL(Master().getCompanyID()),
                " a.sCategrCd = " + SQLUtil.toSQL(Master().getCategoryId()),
                " a.sTransNox LIKE " + SQLUtil.toSQL("%" + fsReferID));
        lsSQL = MiscUtil.addCondition(lsSQL, lsFilterCondition);
        if (!psTranStat.isEmpty()) {
            lsSQL = lsSQL + lsTransStat;
        }
        if (!poGRider.isMainOffice() || !poGRider.isWarehouse()) {
            lsSQL = lsSQL + " AND a.sBranchCd LIKE " + SQLUtil.toSQL(poGRider.getBranchCode());
        }
        lsSQL = lsSQL + " GROUP BY  a.sTransNox"
                + " ORDER BY dTransact ASC";
        System.out.println("Executing SQL: " + lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);

        int lnCtr = 0;
        if (MiscUtil.RecordCount(loRS) >= 0) {
            paInvMaster = new ArrayList<>();
            while (loRS.next()) {
                // Print the result set
                System.out.println("sTransNox: " + loRS.getString("sTransNox"));
                System.out.println("dTransact: " + loRS.getDate("dTransact"));
                System.out.println("sReferNox" + loRS.getString("sReferNox"));
                System.out.println("------------------------------------------------------------------------------");

                paInvMaster.add(INVMasterList());
                paInvMaster.get(paInvMaster.size() - 1).openRecord(loRS.getString("sTransNox"));
                lnCtr++;
            }
            System.out.println("Records found: " + lnCtr);
            loJSON.put("result", "success");
            loJSON.put("message", "Record loaded successfully.");
        } else {
            paInvMaster = new ArrayList<>();
            paInvMaster.add(INVMasterList());
            loJSON.put("result", "error");
            loJSON.put("continue", true);
            loJSON.put("message", "No record found .");
        }
        MiscUtil.close(loRS);
        return loJSON;
    }


      public JSONObject getROQItems() {
         poJSON = new JSONObject();

    try {
        trans = new ROQ(poGRider, poGRider.getBranchCode(), Master().getCategoryId());
        JSONObject loResult = trans.LoadRecommendedOrder();

                if (!"success".equalsIgnoreCase((String) loResult.get("result"))) {
                    poJSON.put("result", "error");
                    poJSON.put("message", "Unable to load ROQ: " + loResult.get("message"));
                    return poJSON;
                }

                CachedRowSet loROQ = trans.getRecommendations();
                loROQ.last();
                int totalROQ = loROQ.getRow();
                loROQ.beforeFirst();
                int index = 0;
                while (loROQ.next()) {
                    String lsStockID = loROQ.getString("sStockIDx");
                    double lnROQ = loROQ.getDouble("nRecOrder");

                    int row = getDetailCount() - 1;
                    Detail(row).setStockId(lsStockID);
                    Detail(row).setRecommendedOrder(lnROQ);

                    index++;
                    if (index < totalROQ) {
                        AddDetail(); 
                    }
                }
                System.out.println(totalROQ + "total roq");
                poJSON.put("result", "success");
                poJSON.put("message", "ROQ items added successfully.");
                
                return poJSON;

            } catch (Exception e) {
                poJSON.put("result", "error");
                poJSON.put("message", "Exception occurred: " + MiscUtil.getException(e));
                return poJSON;
            }
        }



}  
