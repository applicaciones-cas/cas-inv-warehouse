/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class InventoryStockRequestApprovalValidatorFactory {

    public static GValidator make(String industryId) {
        switch (industryId) {
            case "00": //Mobile Phone
                return new InventoryStockRequestApproval_MP();
            case "01": //Motorcycle
                return new InventoryStockRequestApproval_MC();
            case "02":
            case "05":
            case "06": //Vehicle
                return new InventoryStockRequestApproval_Vehicle();
            case "03": //Monarch
                return new InventoryStockRequestApproval_Monarch();
            case "04": //Los Pedritos
                return new InventoryStockRequestApproval_LP();
            case "07":
            case "08":
            case "09": 
            case "10"://General
                return new InventoryStockRequestApproval_General();
            default:
                return new InventoryStockRequestApproval_General();
        }
    }

}
