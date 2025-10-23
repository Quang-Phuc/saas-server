package com.phuclq.student.utils;

import com.phuclq.student.dto.RequestFileDTO;
import com.phuclq.student.types.FileType;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.phuclq.student.common.Constants.BASE64_PDF;

public class SplitDocs {

    private static java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static MultipartFile uploadFile(String base64, String fileName) {
        final String[] base64Array = base64.split(",");
        String dataUir, data;
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            dataUir = "data:image/jpg;base64";
            data = base64Array[0];
        }

        return new Base64ToMultipartFile(dataUir + "," + data, dataUir, fileName);
    }

    public static RequestFileDTO convertDoctoPdf(String base64, String name) throws IOException, Docx4JException {


        java.io.File file = convertMultiPartToFile(uploadFile(base64, name));
        InputStream templateInputStream = new FileInputStream(file);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);

        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        Path path = Paths.get(root.toString(), "src", "main", "resources", "upload", name.replace(".docx", ".pdf").replace(".doc", ".pdf"));
        String outputfilepath = path.toString();
        FileOutputStream os = new FileOutputStream(outputfilepath);
        Docx4J.toPDF(wordMLPackage, os);
        os.flush();
        os.close();


        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bo.writeTo(os);

        File f = new File((path.toString()));
        byte[] content = new byte[(int) f.length()];
        InputStream in = null;

        in = new FileInputStream(f);
        for (int off = 0, read;
             (read = in.read(content, off, content.length - off)) > 0;
             off += read)
            ;

        RequestFileDTO requestFilePdfDto = new RequestFileDTO();
        requestFilePdfDto.setType(FileType.FILE_CONVERT_DOC_PDF.getName());
        requestFilePdfDto.setName(name.replace(".docx", ".pdf").replace(".doc", ".pdf"));
        requestFilePdfDto.setExtension(".pdf");
        requestFilePdfDto.setContent(BASE64_PDF + DatatypeConverter.printBase64Binary(content));
        return requestFilePdfDto;

    }

}
