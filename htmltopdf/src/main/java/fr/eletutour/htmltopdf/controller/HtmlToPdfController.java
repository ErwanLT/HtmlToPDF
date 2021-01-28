package fr.eletutour.htmltopdf.controller;

import fr.eletutour.htmltopdf.services.PDFCreatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
@Slf4j
public class HtmlToPdfController {

    private PDFCreatorService pdfCreator;

    @Autowired
    public HtmlToPdfController(PDFCreatorService pdfCreator){
        this.pdfCreator = pdfCreator;
    }

    @GetMapping("/generateFile")
    @ResponseBody
    public ResponseEntity<InputStreamResource> serveFile() {

        InputStreamResource isr = null;
        HttpHeaders respHeader = new HttpHeaders();

        try {

            File fileToReturn = pdfCreator.genererPDF();


            respHeader.setContentDispositionFormData("attachment", fileToReturn.getName());
            isr = new InputStreamResource(new FileInputStream(fileToReturn));

        } catch (FileNotFoundException e){
            log.error("erreur");
        }

        return new ResponseEntity<>(isr, respHeader, HttpStatus.OK);
    }
}
