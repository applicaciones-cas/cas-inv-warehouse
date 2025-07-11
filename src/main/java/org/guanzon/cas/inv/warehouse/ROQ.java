package org.guanzon.cas.inv.warehouse;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;

public class ROQ {    
    private final GRiderCAS poGRider;
    private final String psCategrCd;
    
    private String psBranchCd;
    private CachedRowSet poROQ;
    private JSONObject poJSON;
    
    /**
     * Recommended Quantity Order Object
     * 
     * @param foValue Application Driver
     * @param fsBranchCd Branch Code
     * @param fsCategrCd  Inventory Category 1
     */
    public ROQ(GRiderCAS foValue, String fsBranchCd, String fsCategrCd){
        poGRider = foValue;
        
        if (psBranchCd == null || psBranchCd.isEmpty()) psBranchCd = poGRider.getBranchCode();
        
        psCategrCd = fsCategrCd;
    }
    
    public CachedRowSet getRecommendations(){
        return poROQ;
    }
    
    public JSONObject LoadRecommendedOrder() throws SQLException{
        return LoadRecommendedOrder(true);
    }
    
    public JSONObject LoadRecommendedOrder(boolean isSerialized) throws SQLException{
        poJSON = new JSONObject();
        
        initROQRowset();
        
        String lsSQL = isSerialized ? getSQ_Serialized() : getSQ_NonSerialized();
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (MiscUtil.RecordCount(loRS) > 0) initRecOrder(loRS);
        
        poJSON.put("result", "success");
        return poJSON;
    }
            
    private void initRecOrder(ResultSet foRS) throws SQLException{
        double lnROQ;
        
        while (foRS.next()){
            lnROQ = computeROQ(foRS);
            
            if (lnROQ > 0) {
                poROQ.last();
                poROQ.updateObject("sStockIDx", foRS.getObject("sStockIDx"));
                poROQ.updateObject("sModelCde", foRS.getObject("sModelCde"));
                poROQ.updateObject("sModelNme", foRS.getObject("sModelNme"));
                poROQ.updateObject("sColorNme", foRS.getObject("sColorNme"));
                poROQ.updateObject("sBrandNme", foRS.getObject("sBrandNme"));
                poROQ.updateObject("sVariantx", foRS.getObject("sVariantx"));
                poROQ.updateObject("nAvgMonSl", foRS.getObject("nAvgMonSl"));
                poROQ.updateObject("nMinLevel", foRS.getObject("nMinLevel"));
                poROQ.updateObject("nMaxLevel", foRS.getObject("nMaxLevel"));
                poROQ.updateObject("cClassify", foRS.getObject("cClassify"));
                poROQ.updateObject("nFloatQty", foRS.getObject("nFloatQty"));
                poROQ.updateObject("nUnconfrm", foRS.getObject("nUnconfrm"));
                poROQ.updateObject("nQuantity", 0.00);
                poROQ.updateObject("nOnTranst", foRS.getObject("nOnTranst"));
                poROQ.updateObject("nQtyOnHnd", foRS.getObject("nQtyOnHnd"));
                poROQ.updateObject("nResvOrdr", foRS.getObject("nResvOrdr"));
                poROQ.updateObject("nBackOrdr", foRS.getObject("nBackOrdr"));
                poROQ.updateObject("nRecOrder", lnROQ);
                poROQ.updateRow();
                
                if (!foRS.isLast()) addROQ();
            }
        }
        
        poROQ.last();
        if (poROQ.getString("sStockIDx") == null || 
            poROQ.getString("sStockIDx").isEmpty()) poROQ.deleteRow();
    }
    
    private double computeROQ(ResultSet foRS) throws SQLException{
        double lnROQ = 0.00;
        
        switch (psCategrCd){
            case "0003": //Motorcycle
                lnROQ = computeROQ_MC(foRS);
                break;
            case "0004": //Motorcycle - Spareparts
                lnROQ = computeROQ_MCSP(foRS);
                break; 
        }       
                
        return lnROQ;
    }
    
    private double computeROQ_MCSP(ResultSet foRS) throws SQLException{
        double lnROQ;
        
        double lnAvailQty = foRS.getDouble("nQtyOnHnd") + 
                                foRS.getDouble("nBackOrdr") + 
                                foRS.getDouble("nFloatQty") + 
                                foRS.getDouble("nUnconfrm");   
                
        if ("ABC".contains(foRS.getString("cClassify"))){
            if (foRS.getDouble("nMinLevel") < foRS.getDouble("nQtyOnHnd")){
                lnROQ = 0.00;
            } else {
                lnROQ = foRS.getDouble("nMaxLevel") - 
                        foRS.getDouble("nResvOrdr") -
                        lnAvailQty;
                
                lnROQ = lnROQ < 0 ? 0 : lnROQ;
            }
        } else {
            lnROQ = foRS.getDouble("nResvOrdr") - lnAvailQty;
            
            lnROQ = lnROQ < 0 ? 0 : lnROQ;
        }
        
        return lnROQ;
    }
    
    private double computeROQ_MC(ResultSet foRS) throws SQLException{
        double lnROQ = 0.00;
        
        double lnAvailQty = foRS.getDouble("nQtyOnHnd") + 
                                foRS.getDouble("nOnTranst") - 
                                foRS.getDouble("nResvOrdr");       
                
        if ("ABC".contains(foRS.getString("cClassify"))){
            if (foRS.getDouble("nMinLevel") > 0){
                if (foRS.getDouble("nMinLevel") > lnAvailQty)
                    lnROQ = foRS.getDouble("nMaxLevel") - lnAvailQty;
            } else {
                if (foRS.getDouble("nMaxLevel") > 0)
                    lnROQ = foRS.getDouble("nMaxLevel") - lnAvailQty;
                else if (lnAvailQty < 0) lnROQ = lnAvailQty * -1;
            }
        } else {
            if (lnAvailQty < 0) lnROQ = lnAvailQty * -1;
        }
        
        return lnROQ;
    }
    
    private void initROQRowset() throws SQLException{
        RowSetMetaData meta = new RowSetMetaDataImpl();
        meta.setColumnCount(23);
        
        meta.setColumnName(1, "sStockIDx");
        meta.setColumnLabel(1, "sStockIDx");
        meta.setColumnType(1, Types.VARCHAR);
        meta.setColumnDisplaySize(1, 12);
        
        meta.setColumnName(2, "sModelCde");
        meta.setColumnLabel(2, "sModelCde");
        meta.setColumnType(2, Types.VARCHAR);
        meta.setColumnDisplaySize(2, 30);
        
        meta.setColumnName(3, "sModelNme");
        meta.setColumnLabel(3, "sModelNme");
        meta.setColumnType(3, Types.VARCHAR);
        meta.setColumnDisplaySize(3, 30);
        
        meta.setColumnName(2, "sModelCde");
        meta.setColumnLabel(2, "sModelCde");
        meta.setColumnType(2, Types.VARCHAR);
        meta.setColumnDisplaySize(2, 30);
        
        meta.setColumnName(4, "sColorNme");
        meta.setColumnLabel(4, "sColorNme");
        meta.setColumnType(4, Types.VARCHAR);
        meta.setColumnDisplaySize(4, 30);
        
        meta.setColumnName(5, "sBrandNme");
        meta.setColumnLabel(5, "sBrandNme");
        meta.setColumnType(5, Types.VARCHAR);
        meta.setColumnDisplaySize(5, 30);
        
        meta.setColumnName(6, "nQuantity");
        meta.setColumnLabel(6, "nQuantity");
        meta.setColumnType(6, Types.DOUBLE);
        
        meta.setColumnName(7, "cClassify");
        meta.setColumnLabel(7, "cClassify");
        meta.setColumnType(7, Types.CHAR);
        meta.setColumnDisplaySize(7, 1);
        
        meta.setColumnName(8, "nRecOrder");
        meta.setColumnLabel(8, "nRecOrder");
        meta.setColumnType(8, Types.DOUBLE);
        
        meta.setColumnName(9, "nQtyOnHnd");
        meta.setColumnLabel(9, "nQtyOnHnd");
        meta.setColumnType(9, Types.DOUBLE);
        
        meta.setColumnName(10, "nResvOrdr");
        meta.setColumnLabel(10, "nResvOrdr");
        meta.setColumnType(10, Types.DOUBLE);
        
        meta.setColumnName(11, "nOnTranst");
        meta.setColumnLabel(11, "nOnTranst");
        meta.setColumnType(11, Types.DOUBLE);
        
        meta.setColumnName(12, "nBackOrdr");
        meta.setColumnLabel(12, "nBackOrdr");
        meta.setColumnType(12, Types.DOUBLE);
        
        meta.setColumnName(13, "nAvgMonSl");
        meta.setColumnLabel(13, "nAvgMonSl");
        meta.setColumnType(13, Types.DOUBLE);
        
        meta.setColumnName(14, "nMinLevel");
        meta.setColumnLabel(14, "nMinLevel");
        meta.setColumnType(14, Types.DOUBLE);
        
        meta.setColumnName(15, "nMaxLevel");
        meta.setColumnLabel(15, "nMaxLevel");
        meta.setColumnType(15, Types.DOUBLE);
        
        meta.setColumnName(16, "nApproved");
        meta.setColumnLabel(16, "nApproved");
        meta.setColumnType(16, Types.DOUBLE);
        
        meta.setColumnName(17, "nIssueQty");
        meta.setColumnLabel(17, "nIssueQty");
        meta.setColumnType(17, Types.DOUBLE);
        
        meta.setColumnName(18, "nOrderQty");
        meta.setColumnLabel(18, "nOrderQty");
        meta.setColumnType(18, Types.DOUBLE);
        
        meta.setColumnName(19, "nAllocQty");
        meta.setColumnLabel(19, "nAllocQty");
        meta.setColumnType(19, Types.DOUBLE);
        
        meta.setColumnName(20, "nReceived");
        meta.setColumnLabel(20, "nReceived");
        meta.setColumnType(20, Types.DOUBLE);
        
        meta.setColumnName(21, "sVariantx");
        meta.setColumnLabel(21, "sVariantx");
        meta.setColumnType(21, Types.VARCHAR);
        meta.setColumnDisplaySize(21, 30);
        
        meta.setColumnName(22, "nFloatQty");
        meta.setColumnLabel(22, "nFloatQty");
        meta.setColumnType(22, Types.DOUBLE);
        
        meta.setColumnName(23, "nUnconfrm");
        meta.setColumnLabel(23, "nUnconfrm");
        meta.setColumnType(23, Types.DOUBLE);        
        
        poROQ = new CachedRowSetImpl();
        poROQ.setMetaData(meta);     
        
        addROQ();
    }
    
    private void addROQ() throws SQLException{
        poROQ.last();
        poROQ.moveToInsertRow();

        MiscUtil.initRowSet(poROQ);
        
        poROQ.updateObject("cClassify", "F");
        
        poROQ.insertRow();
        poROQ.moveToCurrentRow();
    }
       
    private String getSQ_Serialized(){
        return "SELECT" +
                    "  a.sStockIDx" +
                    ", c.sDescript sBrandNme" +
                    ", d.sModelCde sModelCde" +
                    ", d.sDescript sModelNme" +
                    ", f.sDescript sColorNme" +
                    ", e.sDescript sVariantx" +
                    ", a.nMinLevel" +
                    ", a.nMaxLevel" +
                    ", a.nAvgMonSl" +
                    ", a.cClassify" +
                    ", a.nFloatQty" +
                    ", i.nOnTranst" +
                    ", g.nQtyOnHnd" +
                    ", 0.00 nResvOrdr" +
                    ", 0.00 nUnconfrm" +
                    ", h.nBackOrdr" +
                    ", b.nSelPrice" +
                    ", b.sBrandIDx" +
                    ", b.sModelIDx" +
                    ", b.sColorIDx" +
                " FROM Inv_Master a" +
                    ", Inventory b" +
                        " LEFT JOIN Brand c ON b.sBrandIDx = c.sBrandIDx" +
                        " LEFT JOIN Model d ON b.sModelIDx = d.sModelIDx" +
                        " LEFT JOIN Model_Variant e ON b.sVrntIDxx = e.sVrntIDxx" +
                        " LEFT JOIN Color f ON b.sColorIDx = f.sColorIDx" +
                        " LEFT JOIN (" +
                            "SELECT" +
                                "  a.sStockIDx" +
                                ", COUNT(sSerialID) nQtyOnHnd" +
                            " FROM Inv_Serial a" +
                                ", Inventory b" +
                            " WHERE a.sStockIDx = b.sStockIDx" +
                                " AND b.sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +
                                " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                                " AND a.cSoldStat = '0'" +
                            " GROUP BY a.sStockIDx) g ON b.sStockIDx = g.sStockIDx" +
                        " LEFT JOIN (" +
                            "SELECT" +
                                "  a.sStockIDx" +
                                ", SUM(a.nApproved - (a.nCancelld + a.nIssueQty + a.nOrderQty)) nBackOrdr" +
                            " FROM Inv_Stock_Request_Detail a" +
                                ", Inv_Stock_Request_Master b" +
                                ", Inventory c" +
                            " WHERE a.sTransNox = b.sTransNox" +
                                " AND a.sStockIDx = c.sStockIDx" +
                                " AND c.sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +
                                " AND b.sTransNox LIKE " + SQLUtil.toSQL(psBranchCd + "%") +
                                " AND b.cTranStat IN ('0', '1', '2')" +
                            " GROUP BY a.sStockIDx) h ON b.sStockIDx = h.sStockIDx" +
                        " LEFT JOIN (" +
                            "SELECT" +
                                "  a.sStockIDx" +
                                ", COUNT(c.sSerialID) nOnTranst" +
                            " FROM Inv_Transfer_Detail a" +
                                ", Inv_Transfer_Master b" +
                                ", Inv_Serial c" +
                                ", Inventory d" +
                            " WHERE a.sTransNox = b.sTransNox" +
                                " AND a.sSerialID = c.sSerialID" +
                                " AND a.sStockIDx = d.sStockIDx" +
                                " AND d.sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +
                                " AND b.sDestinat  = " + SQLUtil.toSQL(psBranchCd) +
                                " AND b.cTranStat NOT IN ('2', '3')" +
                            " GROUP BY a.sStockIDx) i ON b.sStockIDx = i.sStockIDx" +
                " WHERE a.sStockIDx = b.sStockIDx" +
                    " AND b.sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +
                    " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                    " AND b.cSerialze = '1'" +
                    " AND a.cRecdStat = '1'" +
                " ORDER BY a.cClassify, d.sDescript";

                //TODO: 
                //LEFT JOIN for reserved orders
                //LEFT JOIN for unconfirmed orders
    }
    
    private String getSQ_NonSerialized(){
        return "";
    }
}
