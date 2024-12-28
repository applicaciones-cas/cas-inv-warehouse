package org.guanzon.cas.inv.warehouse.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.InventoryClassification;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.cas.inv.model.Model_Inv_Master;
import org.guanzon.cas.inv.services.InvControllers;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Inv_Stock_Request_Detail extends Model{      
    //reference objects
    Model_Inv_Master poInvMaster;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            
            //assign default values
            poEntity.updateObject("nEntryNox", 0);
            poEntity.updateObject("nQuantity", 0);
            poEntity.updateObject("cClassify", InventoryClassification.NEW_ITEMS);
            poEntity.updateObject("nRecOrder", 0);
            poEntity.updateObject("nQtyOnHnd", 0);
            poEntity.updateObject("nResvOrdr", 0);
            poEntity.updateObject("nBackOrdr", 0);
            poEntity.updateObject("nOnTranst", 0);
            poEntity.updateObject("nAvgMonSl", 0);
            poEntity.updateObject("nMaxLevel", 0);
            poEntity.updateObject("nApproved", 0);
            poEntity.updateObject("nCancelld", 0);
            poEntity.updateObject("nIssueQty", 0);
            poEntity.updateObject("nOrderQty", 0);
            poEntity.updateObject("nAllocQty", 0);
            poEntity.updateObject("nReceived", 0);
            poEntity.updateObject("dModified", "0000-00-00 00:00:00");
            
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sTransNox";
            ID2 = "nEntryNox";
            
            //initialize reference objects
            poInvMaster = InvControllers
            //end - initialize reference objects
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setTransactionNo(String transactionNo){
        return setValue("sTransNox", transactionNo);
    }
    
    public String getTransactionNo  (){
        return (String) getValue("sTransNox");
    }
    
    public JSONObject setBranchCode(String branchCode){
        return setValue("sBranchCd", branchCode);
    }
    
    public String getBranchCode(){
        return (String) getValue("sBranchCd");
    }
    
    public JSONObject setIndustryId(String industryId){
        return setValue("sIndstCdx", industryId);
    }
    
    public String getIndustryId(){
        return (String) getValue("sIndstCdx");
    }
    
    public JSONObject setCategoryId(String categoryId){
        return setValue("sCategrCd", categoryId);
    }
    
    public JSONObject setTransactionDate(Date transactionDate){
        return setValue("dTransact", transactionDate);
    }
    
    public Date getTransactionDate(){
        return (Date) getValue("dTransact");
    }
        
    public JSONObject setReferenceNo(String referenceNo){
        return setValue("sReferNox", referenceNo);
    }
    
    public String getReferenceNo(){
        return (String) getValue("sReferNox");
    }
    
    public JSONObject setRemarks(String remarks){
        return setValue("sRemarksx", remarks);
    }
    
    public String getRemarks(){
        return (String) getValue("sRemarksx");
    }
    
    public JSONObject setIssuanceNotes(String issuanceNotes){
        return setValue("sIssNotes", issuanceNotes);
    }
    
    public String getIssuanceNotes(){
        return (String) getValue("sIssNotes");
    }
    
    public JSONObject setCurrentInventory(int quantity){
        return setValue("nCurrInvx", quantity);
    }
    
    public int getCurrentInventory(){
        return (int) getValue("nCurrInvx");
    }
    
    public JSONObject setEstimateInventory(int quantity){
        return setValue("nEstInvxx", quantity);
    }
    
    public int getEstimateInventory(){
        return (int) getValue("nEstInvxx");
    }
    
    public JSONObject setApproverId(String approverId){
        return setValue("sApproved", approverId);
    }
    
    public String getApproverId(){
        return (String) getValue("sApproved");
    }
    
    public JSONObject setApprovalDate(Date approvalDate){
        return setValue("dApproved", approvalDate);
    }
    
    public Date getApprovalDate(){
        return (Date) getValue("dApproved");
    }
    
    public JSONObject setApprovalCode(String approvalCode){
        return setValue("sAprvCode", approvalCode);
    }
    
    public String getApprovalCode(){
        return (String) getValue("sAprvCode");
    }
    
    public JSONObject setEntryNo(int rows){
        return setValue("nEntryNox", rows);
    }
    
    public int getEntryNo(){
        return (int) getValue("nEntryNox");
    }
    
    public JSONObject setSourceCode(String sourceCode){
        return setValue("sSourceCd", sourceCode);
    }
    
    public String getSourceCode(){
        return (String) getValue("sSourceCd");
    }
    
    public JSONObject setSourceNo(String sourceNo){
        return setValue("sSourceNo", sourceNo);
    }
    
    public String getSourceNo(){
        return (String) getValue("sSourceNo");
    }
    
    public JSONObject isConfirmed(boolean isConfirmed){
        return setValue("cConfirmd", isConfirmed ? "1" : "0");
    }

    public boolean isisConfirmed(){
        return ((String) getValue("cConfirmd")).equals("1");
    }
        
    public JSONObject setTransactionStatus(String transactionStatus){
        return setValue("cTranStat", transactionStatus);
    }
    
    public String getTransactionStatus(){
        return (String) getValue("cTranStat");
    }
    
    public JSONObject setModifyingId(String modifyingId){
        return setValue("sModified", modifyingId);
    }
    
    public String getModifyingId(){
        return (String) getValue("sModified");
    }
    
    public JSONObject setModifiedDate(Date modifiedDate){
        return setValue("dModified", modifiedDate);
    }
    
    public Date getModifiedDate(){
        return (Date) getValue("dModified");
    }
    
    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(this.getTable(), ID, true, poGRider.getConnection(), poGRider.getBranchCode());
    }
    
    //reference object models
    public Model_Branch Branch() {
        if (!"".equals((String) getValue("sBranchCd"))) {
            if (poBranch.getEditMode() == EditMode.READY
                    && poBranch.getBranchCode().equals((String) getValue("sBranchCd"))) {
                return poBranch;
            } else {
                poJSON = poBranch.openRecord((String) getValue("sBranchCd"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poBranch;
                } else {
                    poBranch.initialize();
                    return poBranch;
                }
            }
        } else {
            poBranch.initialize();
            return poBranch;
        }
    }
    
    public Model_Category Industry() {
        if (!"".equals((String) getValue("sIndstCdx"))) {
            if (poIndustry.getEditMode() == EditMode.READY
                    && poIndustry.getCategoryId().equals((String) getValue("sIndstCdx"))) {
                return poIndustry;
            } else {
                poJSON = poIndustry.openRecord((String) getValue("sIndstCdx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poIndustry;
                } else {
                    poIndustry.initialize();
                    return poIndustry;
                }
            }
        } else {
            poIndustry.initialize();
            return poIndustry;
        }
    }
    
    public Model_Category_Level2 Category() {
        if (!"".equals((String) getValue("sCategrCd"))) {
            if (poCategory.getEditMode() == EditMode.READY
                    && poCategory.getCategoryId().equals((String) getValue("sCategrCd"))) {
                return poCategory;
            } else {
                poJSON = poCategory.openRecord((String) getValue("sCategrCd"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poCategory;
                } else {
                    poCategory.initialize();
                    return poCategory;
                }
            }
        } else {
            poCategory.initialize();
            return poCategory;
        }
    }
    //end - reference object models
}