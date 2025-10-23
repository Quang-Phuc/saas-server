package com.phuclq.student.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.phuclq.student.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.requireNonNull;

@Slf4j
public class FileUtils {

    private FileUtils() {
    }

    public static String doubleStringToLongString(String doubleString) {
        if (StringUtils.isEmpty(doubleString)) {
            doubleString = null;
        } else if (doubleString.endsWith(".0")) {
            doubleString = StringUtils.replace(doubleString, ".0", Constants.VN_MONEY_SURFIX);
        } else {
            doubleString = doubleString + Constants.VN_MONEY_SURFIX;
        }
        return doubleString;
    }

    public static String multipartFileToBase64Ver1(MultipartFile multipart) throws IOException {
        byte[] image = Base64.encodeBase64(multipart.getBytes());
        return new String(image);
    }

    public static Boolean validateListFile(List<MultipartFile> multipartFiles) {

        for (MultipartFile file : multipartFiles) {
            if (!file.isEmpty()
                    && !requireNonNull(file.getContentType())
                    .toLowerCase(Locale.ROOT)
                    .contains(Constants.IMAGE)
                    && !requireNonNull(file.getContentType())
                    .toLowerCase(Locale.ROOT)
                    .endsWith(Constants.PDF.toLowerCase(Locale.ROOT))
                    || requireNonNull(file.getContentType()) // chưa merge dc file kiểu image/webp
                    .toLowerCase(Locale.ROOT)
                    .endsWith(Constants.WEBP)) {
                return false;
            }
        }
        return true;
    }

    public static String mergeFileToBase64(String base64Image)
            throws IOException, DocumentException {
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        log.info("[FILE] start");
        PDDocument document;
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        mergerUtility.setDestinationStream(response);
        ByteArrayOutputStream baos;

//      BufferedImage bimg = ImageIO.read(file.getInputStream());
        BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (bimg != null) {
            BufferedImage resizeImage = resizeImage(bimg, new BigDecimal(2000), new BigDecimal(1125));
            document = new PDDocument();
            baos = new ByteArrayOutputStream();
            PDPage page = new PDPage(new PDRectangle(resizeImage.getWidth(), resizeImage.getHeight()));
            document.addPage(page);
            PDImageXObject img = LosslessFactory.createFromImage(document, resizeImage);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(img, 0, 0);
            }
            document.save(baos);
            document.close();
            mergerUtility.addSource(new ByteArrayInputStream(baos.toByteArray()));
        } else {
            //                mergerUtility.addSource(file.getInputStream());
            mergerUtility.addSource(new ByteArrayInputStream(imageBytes));
        }
        mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        byte[] bytes = Base64.encodeBase64(response.toByteArray());
        log.info("[FILE] end");
        return new String(bytes);
    }

    public static BufferedImage resizeImage(
            BufferedImage originalImage, BigDecimal targetWidth, BigDecimal targetHeight) {
        BufferedImage scaledImg = Scalr.resize(originalImage, Method.SPEED, targetWidth.intValue(),
                targetHeight.intValue());
        BufferedImage resizedImage =
                new BufferedImage(
                        targetWidth.intValue(), targetHeight.intValue(), BufferedImage.BITMASK);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(
                scaledImg, 0, 0, targetWidth.intValue(), targetHeight.intValue(), null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static InputStream resizePDF(
            InputStream inputStreamPdf, float width, float height, float tolerance)
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStreamPdf);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            com.itextpdf.text.Rectangle cropBox = reader.getCropBox(i);
            float widthToAdd = width - cropBox.getWidth();
            float heightToAdd = height - cropBox.getHeight();
            if (Math.abs(widthToAdd) > tolerance || Math.abs(heightToAdd) > tolerance) {
                float[] newBoxValues =
                        new float[]{
                                cropBox.getLeft() - widthToAdd / 2,
                                cropBox.getBottom() - heightToAdd / 2,
                                cropBox.getRight() + widthToAdd / 2,
                                cropBox.getTop() + heightToAdd / 2
                        };
                PdfArray newBox = new PdfArray(newBoxValues);

                PdfDictionary pageDict = reader.getPageN(i);
                pageDict.put(PdfName.CROPBOX, newBox);
                pageDict.put(PdfName.MEDIABOX, newBox);
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfStamper stamper = new PdfStamper(reader, out);
        stamper.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


}