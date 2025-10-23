package com.phuclq.student.utils;

import com.itextpdf.text.DocumentException;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.codec.Base64;

public class FileProcessor {

    public static MultipartFile cutAndSelectPages(Integer startPageNumber, Integer endPageNumber, MultipartFile multipartFile) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(multipartFile.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        int n = reader.getNumberOfPages();
        int endPage = Math.min(endPageNumber, n);
        String viewPage = startPageNumber + "-" + endPage;
        reader.selectPages(viewPage);
        stamper.close();
        byte[] cutFileBytes = baos.toByteArray();
        return createMultipartFile(cutFileBytes, multipartFile.getOriginalFilename(), multipartFile.getContentType());
    }

    private static MultipartFile createMultipartFile(byte[] content, String originalFilename, String contentType) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return originalFilename;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return content;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(content);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                // Not implemented
            }
        };
    }
    public static MultipartFile convertDocToPdf(MultipartFile docFile, String name) throws IOException, Docx4JException {
        InputStream templateInputStream = docFile.getInputStream();
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Docx4J.toPDF(wordMLPackage, pdfOutputStream);

        // Convert ByteArrayOutputStream to byte array
        byte[] pdfContent = pdfOutputStream.toByteArray();

        // Create MultipartFile from byte array
        return new MultipartFile() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getOriginalFilename() {
                return name.replace(".docx", ".pdf").replace(".doc", ".pdf");
            }

            @Override
            public String getContentType() {
                return "application/pdf";
            }

            @Override
            public boolean isEmpty() {
                return pdfContent.length == 0;
            }

            @Override
            public long getSize() {
                return pdfContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return pdfContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(pdfContent);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                // Not implemented
            }
        };
    }
}
