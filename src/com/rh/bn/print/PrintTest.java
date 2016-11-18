package com.rh.bn.print;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterLocation;

public class PrintTest {
    
    public static void main(String[] args){
        try {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PrintRequestAttribute attr = new Destination(new URI("172.16.0.54"));
        aset.add(attr);
        aset.add(MediaSizeName.ISO_A4);
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
        for(PrintService ps : pservices){
            System.out.println(ps.getName());
        }
        if(pservices.length > 0){
                DocPrintJob printJob = pservices[0].createPrintJob();
                FileInputStream fis;
                fis = new FileInputStream("D:\\WinPrinter.doc");
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                printJob.print(doc, aset);
        }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PrintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
//        PrintService service = ServiceUI.printDialog(null, 200, 200, pservices, defaultService, flavor, aset);
//        if (service != null) {
//            try {
//                DocPrintJob pj = service.createPrintJob();
//                Attribute attr = new PrinterLocation("172.16.0.54",Locale.getDefault());
//                aset.add(MediaSizeName.ISO_A4);
//                FileInputStream fis = new FileInputStream("D:\\WinPrinter.txt");
//                DocAttributeSet das = new HashDocAttributeSet();
//                Doc doc = new SimpleDoc(fis, flavor, das);
//                pj.print(doc, aset);
//                Thread.sleep(10 * 1000);
//            } catch (FileNotFoundException fe) {
//                fe.printStackTrace();
//            } catch (PrintException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
 catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
