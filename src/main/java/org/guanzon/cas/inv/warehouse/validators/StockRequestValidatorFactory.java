package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class StockRequestValidatorFactory {
    public static GValidator make(String industryId){
        switch (industryId) {
            case "01": //Mobile Phone
                return new StockRequest_MP();
            case "02": //Motorcycle
                return new StockRequest_MC();
            case "03": //Vehicle
                return new StockRequest_Vehicle();
            case "04": //Hospitality
                return new StockRequest_Hospitality();
            case "05": //Los Pedritos
                return new StockRequest_LP();
            default:
                return null;
        }
    }
}