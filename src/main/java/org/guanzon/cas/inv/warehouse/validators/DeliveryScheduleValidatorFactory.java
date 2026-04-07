/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.cas.inv.warehouse.validators;

import org.guanzon.appdriver.iface.GValidator;

public class DeliveryScheduleValidatorFactory {

    public static GValidator make(String industryId) {
        switch (industryId) {
            case "00": //Mobile Phone & Appliance
                return new DeliverySchedule_MP();
            case "01": //Motorcycle
                return new DeliverySchedule_MC();
            case "02": //Vehicle
            case "05":
            case "06":
                return new DeliverySchedule_Vehicle();
            case "03": //Hospitality
                return new DeliverySchedule_Hospitality();
            case "04": //Los Pedritos
                return new DeliverySchedule_LP();
            case "07": //General
            case "08":
            case "09":
            case "10":
                return new DeliverySchedule_General();
                
            default: //Main Office
                return new DeliverySchedule_General();
        }
    }

}
