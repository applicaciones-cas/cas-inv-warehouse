package org.guanzon.cas.inv.warehouse;

import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inv.warehouse.model.Model_Inv_Stock_Request_Master;

public class StockRequest extends Transaction{
    private final String SOURCE_CODE = "InvR";
    
    Model_Inv_Stock_Request_Master poMaster;
    Model_Inv_Stock_Request_Detail poDetail;
    
    @Override
    public String getSourceCode(){
        return SOURCE_CODE;
    }    
}