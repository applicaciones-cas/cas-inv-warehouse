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
            poEntity.updateObject("nQuantity", 0.00);
            poEntity.updateObject("cClassify", InventoryClassification.NEW_ITEMS);
            poEntity.updateObject("nRecOrder", 0.00);
            poEntity.updateObject("nQtyOnHnd", 0.00);
            poEntity.updateObject("nResvOrdr", 0.00);
            poEntity.updateObject("nBackOrdr", 0.00);
            poEntity.updateObject("nOnTranst", 0);
            poEntity.updateObject("nAvgMonSl", 0);
            poEntity.updateObject("nMaxLevel", 0);
            poEntity.updateObject("nApproved", 0.00);
            poEntity.updateObject("nCancelld", 0.00);
            poEntity.updateObject("nIssueQty", 0.00);
            poEntity.updateObject("nOrderQty", 0.00);
            poEntity.updateObject("nAllocQty", 0.00);
            poEntity.updateObject("nReceived", 0.00);
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

    public JSONObject setQuantity(Number quantity) {
        return setValue("nQuantity", quantity);
    }

    public Number getQuantity() {
        return (Number) getValue("nQuantity");
    }

    public JSONObject setClassification(String classification) {
        return setValue("cClassify", classification);
    }

    public String getClassification() {
        return (String) getValue("cClassify");
    }

    public JSONObject setRecommendedOrder(Number quantity) {
        return setValue("nRecOrder", quantity);
    }

    public Number getRecommendedOrder() {
        return (Number) getValue("nRecOrder");
    }

    public JSONObject setQuantityOnHand(Number quantity) {
        return setValue("nQtyOnHnd", quantity);
    }

    public Number getQuantityOnHand() {
        return (Number) getValue("nQtyOnHnd");
    }

    public JSONObject setReservedOrder(Number quantity) {
        return setValue("nResvOrdr", quantity);
    }

    public Number getReservedOrder() {
        return (Number) getValue("nResvOrdr");
    }

    public JSONObject setBackOrder(Number quantity) {
        return setValue("nBackOrdr", quantity);
    }

    public Number getBackOrder() {
        return (Number) getValue("nBackOrdr");
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

    public Number getApproved() {
        return (Number) getValue("nApproved");
    }

    public JSONObject setCancelled(Number quantity) {
        return setValue("nCancelld", quantity);
    }

    public Number getCancelled() {
        return (Number) getValue("nCancelld");
    }

    public JSONObject setIssued(Number quantity) {
        return setValue("nIssueQty", quantity);
    }

    public Number getIssued() {
        return (Number) getValue("nIssueQty");
    }

    public JSONObject setPurchase(Number quantity) {
        return setValue("nOrderQty", quantity);
    }

    public Number getPurchase() {
        return (Number) getValue("nOrderQty");
    }

    public JSONObject setAllocated(int quantity) {
        return setValue("nAllocQty", quantity);
    }

    public Number getAllocated() {
        return (Number) getValue("nAllocQty");
    }

    public JSONObject setReceived(Number quantity) {
        return setValue("nReceived", quantity);
    }

    public Number getReceived() {
        return (Number) getValue("nReceived");
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
                poJSON = poInvMaster.openRecord((String) getValue("sStockIDx"));

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
    public JSONObject openRecordByReference(String Id1, Object Id2) throws SQLException, GuanzonException {
        poJSON = new JSONObject();

        String lsSQL = MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, "sTransNox = " + SQLUtil.toSQL(Id1)
                + " AND sStockIDx = " + SQLUtil.toSQL(Id2));

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
