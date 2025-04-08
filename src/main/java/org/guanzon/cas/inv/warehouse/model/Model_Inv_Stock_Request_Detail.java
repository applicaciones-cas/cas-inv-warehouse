package org.guanzon.cas.inv.warehouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.InventoryClassification;
import org.guanzon.cas.inv.model.Model_Inv_Master;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.guanzon.cas.inv.services.InvModels;
import org.json.simple.JSONObject;

public class Model_Inv_Stock_Request_Detail extends Model {

    //reference objects
    Model_Inv_Master poInvMaster;
    Model_Inventory poInventory;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateObject("sStockIDx", "");
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
            InvModels modelInv = new InvModels(poGRider);
            poInvMaster = modelInv.InventoryMaster();
            poInventory = modelInv.Inventory();
            //end - initialize reference objects
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public String getNextCode() {
        return "";
    }

    public JSONObject setTransactionNo(String transactionNo) {
        return setValue("sTransNox", transactionNo);
    }

    public String getTransactionNo() {
        return (String) getValue("sTransNox");
    }

    public JSONObject setEntryNumber(int entryNumber) {
        return setValue("nEntryNox", entryNumber);
    }

    public Integer getEntryNumber() {
        return (int) getValue("nEntryNox");
    }

    public JSONObject setStockId(String stockId) {
        return setValue("sStockIDx", stockId);
    }

    public String getStockId() {
        return (String) getValue("sStockIDx");
    }

    public JSONObject setQuantity(int quantity) {
        return setValue("nQuantity", quantity);
    }

    public int getQuantity() {
        return (int) getValue("nQuantity");
    }

    public JSONObject setClassification(String classification) {
        return setValue("cClassify", classification);
    }

    public String getClassification() {
        return (String) getValue("cClassify");
    }

    public JSONObject setRecommendedOrder(int quantity) {
        return setValue("nRecOrder", quantity);
    }

    public int getRecommendedOrder() {
        return (int) getValue("nRecOrder");
    }

    public JSONObject setQuantityOnHand(int quantity) {
        return setValue("nQtyOnHnd", quantity);
    }

    public int getQuantityOnHand() {
        return (int) getValue("nQtyOnHnd");
    }

    public JSONObject setReservedOrder(int quantity) {
        return setValue("nResvOrdr", quantity);
    }

    public int getReservedOrder() {
        return (int) getValue("nResvOrdr");
    }

    public JSONObject setBackOrder(int quantity) {
        return setValue("nBackOrdr", quantity);
    }

    public int getBackOrder() {
        return (int) getValue("nBackOrdr");
    }

    public JSONObject setOnTransit(int quantity) {
        return setValue("nOnTranst", quantity);
    }

    public int getOnTransit() {
        return (int) getValue("nOnTranst");
    }

    public JSONObject setAverageMonthlySale(int quantity) {
        return setValue("nAvgMonSl", quantity);
    }

    public int getAverageMonthlySale() {
        return (int) getValue("nAvgMonSl");
    }

    public JSONObject setMaxLevel(int quantity) {
        return setValue("nMaxLevel", quantity);
    }

    public int getMaxLevel() {
        return (int) getValue("nMaxLevel");
    }

    public JSONObject setApproved(int quantity) {
        return setValue("nApproved", quantity);
    }

    public int getApproved() {
        return (int) getValue("nApproved");
    }

    public JSONObject setCancelled(int quantity) {
        return setValue("nCancelld", quantity);
    }

    public int getCancelled() {
        return (int) getValue("nCancelld");
    }

    public JSONObject setIssued(int quantity) {
        return setValue("nIssueQty", quantity);
    }

    public int getIssued() {
        return (int) getValue("nIssueQty");
    }

    public JSONObject setPurchase(int quantity) {
        return setValue("nOrderQty", quantity);
    }

    public int getPurchase() {
        return (int) getValue("nOrderQty");
    }

    public JSONObject setAllocated(int quantity) {
        return setValue("nAllocQty", quantity);
    }

    public int getAllocated() {
        return (int) getValue("nAllocQty");
    }

    public JSONObject setReceived(int quantity) {
        return setValue("nReceived", quantity);
    }

    public int getReceived() {
        return (int) getValue("nReceived");
    }

    public JSONObject setNotes(String notes) {
        return setValue("sNotesxxx", notes);
    }

    public String getNotes() {
        return (String) getValue("sNotesxxx");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }

    //reference object models
    public Model_Inv_Master InvMaster() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sStockIDx"))) {
            if (poInvMaster.getEditMode() == EditMode.READY
                    && poInvMaster.getStockId().equals((String) getValue("sStockIDx"))) {
                return poInvMaster;
            } else {
                poJSON= poInvMaster.openRecord((String) getValue("sStockIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInvMaster;
                } else {
                    poInvMaster.initialize();
                    return poInvMaster;
                }
            }
        } else {
            poInvMaster.initialize();
            return poInvMaster;
        }
    }

    public Model_Inventory Inventory() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sStockIDx"))) {
            if (poInventory.getEditMode() == EditMode.READY
                    && poInventory.getStockId().equals((String) getValue("sStockIDx"))) {
                return poInventory;
            } else {
                poJSON = poInventory.openRecord((String) getValue("sStockIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInventory;
                } else {
                    poInvMaster.initialize();
                    return poInventory;
                }
            }
        } else {
            poInventory.initialize();
            return poInventory;
        }
    }
    //end - reference object models
    public JSONObject openRecordByReference(String Id1, Object Id2) throws SQLException, GuanzonException  {
        poJSON = new JSONObject();

        String lsSQL = MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, "sTransNox = " + SQLUtil.toSQL(Id1) +
                                                " AND sStockIDx = " + SQLUtil.toSQL(Id2));

        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()) {
                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++) {
                    setValue(lnCtr, loRS.getObject(lnCtr));
                }
                
                MiscUtil.close(loRS);

                pnEditMode = EditMode.READY;

                poJSON = new JSONObject();
                poJSON.put("result", "success");
                poJSON.put("message", "Record loaded successfully.");
            } else {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "No record to load.");
            }
        } catch (SQLException e) {
//            logError(getCurrentMethodName() + "»" + e.getMessage());
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }
}