package com.vkim.javelin;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

public class App {

  public static byte[] generatePdfPrintForm(byte[] jrxmlFile, byte[] xmlData) throws JRException {
    JasperReport jasperReport = JasperCompileManager
        .compileReport(new ByteArrayInputStream(jrxmlFile));
    Document document = JRXmlUtils.parse(new ByteArrayInputStream(xmlData));
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null,
        new JRXmlDataSource(document, jasperReport.getQuery().getText()));
    return JasperExportManager.exportReportToPdf(jasperPrint);
  }


  public static void main(String[] args) throws IOException, JRException {
    String xmlFilePath = "C:\\Users\\vitalii_kim\\Documents\\workspace\\metl-mbs\\jasperreports\\pension\\OrderDataExample.xml";
    String jrxmlFilePath = "C:\\Users\\vitalii_kim\\Documents\\workspace\\metl-mbs\\jasperreports\\pension\\Policy_certificate.jrxml";

    byte[] file = generatePdfPrintForm(IOUtils.toByteArray(new FileInputStream(jrxmlFilePath)),
        IOUtils.toByteArray(new FileInputStream(xmlFilePath)));
    IOUtils.write(file,
        new FileOutputStream("C:\\Users\\vitalii_kim\\Desktop\\pension\\printForm.pdf"));
  }
}