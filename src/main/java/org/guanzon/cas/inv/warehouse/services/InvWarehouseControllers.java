package org.guanzon.cas.inv.warehouse.services;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.cas.inv.warehouse.StockRequest;
public class InvWarehouseControllers {
    public InvWarehouseControllers(GRider applicationDriver, LogWrapper logWrapper){
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }
    
    public StockRequest StockRequest(){
        if (poGRider == null){
            poLogWrapper.severe("InvWarehouseControllers.StockRequest: Application driver is not set.");
            return null;
        }
        
        if (poStockRequest != null) return poStockRequest;
        
        poStockRequest = new StockRequest();
        poStockRequest.setApplicationDriver(poGRider);
        poStockRequest.setBranchCode(poGRider.getBranchCode());
        poStockRequest.setVerifyEntryNo(true);
        poStockRequest.setWithParent(false);
        poStockRequest.setLogWrapper(poLogWrapper);
        return poStockRequest;        
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            poStockRequest = null;
                    
            poLogWrapper = null;
            poGRider = null;
        } finally {
            super.finalize();
        }
    }
    
    private GRider poGRider;
    private LogWrapper poLogWrapper;
    
    private StockRequest poStockRequest;
}
