package com.neuracare.backend.ocr;

import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OCRService {

    public String extractText(File file) {

        try {

            String fileName = file.getName().toLowerCase();

            // PDF
            if (fileName.endsWith(".pdf")) {

                PDDocument document = PDDocument.load(file);

                PDFTextStripper stripper = new PDFTextStripper();

                String text = stripper.getText(document);

                document.close();

                return text;
            }

            // Image OCR
            Tesseract tesseract = new Tesseract();

            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            tesseract.setLanguage("eng");

            return tesseract.doOCR(file);

        } catch (Exception e) {

            throw new RuntimeException("OCR extraction failed");

        }

    }

}