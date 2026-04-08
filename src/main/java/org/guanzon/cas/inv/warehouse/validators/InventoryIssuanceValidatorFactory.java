/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class InventoryIssuanceValidatorFactory {

    public static GValidator make(String industryId) {
        switch (industryId) {
            case "00": //Mobile Phone
                return new InventoryStockIssuance_MP();
            case "01": //Motorcycle
                return new InventoryStockIssuance_MC();
            case "02": //Vehicle
            case "05":
            case "06":
                return new InventoryStockIssuance_Vehicle();
            case "03": //Monarch
                return new InventoryStockIssuance_Monarch();
            case "04": //Los Pedritos
                return new InventoryStockIssuance_LP();
            case "07": //Main Office / General
            case "08":
            case "09":
            case "10":
                return new InventoryStockIssuance_General();

            case "":
                return new InventoryStockIssuance_Appliance();
            default:
                return new InventoryStockIssuance_General();
        }
    }

}
