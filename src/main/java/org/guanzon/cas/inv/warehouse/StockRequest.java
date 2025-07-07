package org.guanzon.cas.inv.warehouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
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
    List<Model> mod;
    List<Model_Inv_Stock_Request_Master> paInvMaster;
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
    public Model_Inv_Stock_Request_Master INVMaster(int row) {
        return (Model_Inv_Stock_Request_Master) paInvMaster.get(row);
    }

    public int getINVMasterCount() {
        return this.paInvMaster.size();
    }
    private Model_Inv_Stock_Request_Master INVMasterList() {
        return new InvWarehouseModels(poGRider).InventoryStockRequestMaster();
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
       Detail(row).setCategoryCode(object.getModel().Inventory().getCategoryFirstLevelId());
        
     
    }

    return poJSON;
}

 public JSONObject SearchBarcode(String value, boolean byCode, int row, String brandId)
               throws ExceptionInInitializerError, SQLException, GuanzonException, CloneNotSupportedException, NullPointerException {

         InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
        object.setRecordStatus(RecordStatus.ACTIVE);
 
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
                    }
                }
            }
            Detail(row).setStockId(object.Inventory().getModel().getStockId());
            
        }
        return poJSON;
    
    }
  public JSONObject SearchBarcodeDescription(String value, boolean byCode, int row, String brandId) throws ExceptionInInitializerError, SQLException, GuanzonException, CloneNotSupportedException,
            NullPointerException {
        InvMaster object = new InvControllers(poGRider, logwrapr).InventoryMaster();
        object.setRecordStatus(RecordStatus.ACTIVE);
        
         poJSON = object.Inventory().searchRecord(value, byCode, null, brandId, Master().getIndustryId(),Master().getCategoryId());
        
//         String categID = Master().getCategoryId();
//        poJSON = object.Inventory().searchRecord(value, byCode, null, brandId, industry,categID);
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
                    || (double) item.getValue("nQuantity") <= 0) {
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
            double quantity = Detail(lnRow).getQuantity();
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
    
    
                  
        @Override
public void initSQL() {
    SQL_BROWSE = "SELECT " +
                 "  a.sTransNox, " +
                 "  a.dTransact, " +
                 "  a.sIndstCdx, " +     
                 "  a.sReferNox " +       
                 "FROM Inv_Stock_Request_Master a";
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
           System.out.println("CATEGGGGGG "+Master().getCategoryId());
          
        initSQL();
        String lsSQL = MiscUtil.addCondition(SQL_BROWSE, " a.sIndstCdx = " + SQLUtil.toSQL(Master().getIndustryId())
               //+ " AND a.sCompnyID = " + SQLUtil.toSQL(poGRider.getCompnyId())
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
                //" a.sCompnyID = " + SQLUtil.toSQL(Master().getCompanyID()),
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




}  
