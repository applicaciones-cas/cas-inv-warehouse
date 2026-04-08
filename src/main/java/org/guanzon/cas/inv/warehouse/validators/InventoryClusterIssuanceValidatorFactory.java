/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class InventoryClusterIssuanceValidatorFactory {

    public static GValidator make(String industryId) {
        switch (industryId) {
            case "00": //Mobile Phone
                return new InventoryStockClusterIssuance_MP();
            case "01": //Motorcycle
                return new InventoryStockClusterIssuance_MC();
            case "02":
            case "05":
            case "06": //Vehicle
                return new InventoryStockClusterIssuance_Vehicle();
            case "03": //Monarch
                return new InventoryStockClusterIssuance_Monarch();
            case "04": //Los Pedritos
                return new InventoryStockClusterIssuance_LP();
            case "07": 
            case "08":
            case "09":
            case "10"://General / Main Office
                return new InventoryStockClusterIssuance_General();
            default:
                return new InventoryStockClusterIssuance_General();
        }
    }

}
