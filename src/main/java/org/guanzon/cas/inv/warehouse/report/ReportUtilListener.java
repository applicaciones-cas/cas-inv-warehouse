/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inv.warehouse.report;

/**
 *
 * @author Maynard
 */
public interface ReportUtilListener {

    void onReportOpen();

    void onReportClose();

    void onReportPrint();

    void onReportExport();

    void onReportExportPDF();
}
