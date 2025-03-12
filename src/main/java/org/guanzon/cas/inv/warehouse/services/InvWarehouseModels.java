package org.guanzon.cas.inv.warehouse.services;

import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Master;

public class InvWarehouseModels {
    public InvWarehouseModels(GRiderCAS applicationDriver){
        poGRider = applicationDriver;
    }
    
    public Model_Inv_Stock_Request_Master InventoryStockRequestMaster(){
        if (poGRider == null){
            System.err.println("InvWarehouseModels.InventoryStockRequestMaster: Application driver is not set.");
            return null;
        }
        
        if (poInvRequestMaster == null){
            poInvRequestMaster = new Model_Inv_Stock_Request_Master();
            poInvRequestMaster.setApplicationDriver(poGRider);
            poInvRequestMaster.setXML("Model_Inv_Stock_Request_Master");
            poInvRequestMaster.setTableName("Inv_Stock_Request_Master");
            poInvRequestMaster.initialize();
        }

        return poInvRequestMaster;
    }
    
    public Model_Inv_Stock_Request_Detail InventoryStockRequestDetail(){
        if (poGRider == null){
            System.err.println("InvWarehouseModels.InventoryStockRequestDetail: Application driver is not set.");
            return null;
        }
        
        if (poInvRequestDetail == null){
            poInvRequestDetail = new Model_Inv_Stock_Request_Detail();
            poInvRequestDetail.setApplicationDriver(poGRider);
            poInvRequestDetail.setXML("Model_Inv_Stock_Request_Detail");
            poInvRequestDetail.setTableName("Inv_Stock_Request_Detail");
            poInvRequestDetail.initialize();
        }

        return poInvRequestDetail;
    }
    
    private final GRiderCAS poGRider;
    
    private Model_Inv_Stock_Request_Master poInvRequestMaster;
    private Model_Inv_Stock_Request_Detail poInvRequestDetail;
}
