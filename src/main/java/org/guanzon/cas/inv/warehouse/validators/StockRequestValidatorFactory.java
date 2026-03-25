package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class StockRequestValidatorFactory {
    public static GValidator make(String industryId){
        switch (industryId) {
            case "00": //Mobile Phone
                return new StockRequest_MP();
            case "01": //Motorcycle
                return new StockRequest_MC();
            case "02": //Vehicle
                return new StockRequest_Vehicle();
            case "03": //Hospitality
                return new StockRequest_Hospitality();
            case "04": //Los Pedritos
                return new StockRequest_LP();
            default:
                return new StockRequest_General();
        }
    }
}
