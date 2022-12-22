package com.jesperancinha.ptd;

import lombok.val;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;

public class PublicTransporterLauncher {

    public static void main(String[] args) throws TikaException, IOException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        val inputstream = PublicTransporterLauncher.class.getResourceAsStream("/declaratieoverzicht_22122022110627.pdf");
        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata, pcontext);

        //getting the content of the document
        System.out.println("Contents of the PDF :" + handler.toString());

        //getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + " : " + metadata.get(name));
        }
    }
}
