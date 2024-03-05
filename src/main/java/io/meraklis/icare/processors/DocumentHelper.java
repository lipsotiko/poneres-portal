package io.meraklis.icare.processors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DocumentHelper {

    public static void processField(PDField field, String sLevel, String sParent) {
        String partialName = field.getPartialName();

        if (field instanceof PDNonTerminalField) {
            if (!sParent.equals(field.getPartialName()) && partialName != null) {
                sParent = sParent + "." + partialName;
            }
            System.out.println(sLevel + sParent);

            for (PDField child : ((PDNonTerminalField) field).getChildren()) {
                processField(child, "|  " + sLevel, sParent);
            }
        } else {
            String fieldValue = field.getValueAsString();
            StringBuilder outputString = new StringBuilder(sLevel);
            outputString.append(sParent);
            if (partialName != null) {
                outputString.append(".").append(partialName);
            }
            outputString.append(" = ").append(fieldValue);
            outputString.append(",  type=").append(field.getClass().getName());
            System.out.println(outputString);
        }
    }

    public static void setField(PDDocument pdfDocument, String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {
            if (field instanceof PDCheckBox checkbox) {
                if (value.isEmpty()) {
                    checkbox.unCheck();
                } else {
                    checkbox.check();
                }
            } else if (field instanceof PDComboBox) {
                field.setValue(value);
            } else if (field instanceof PDListBox) {
                field.setValue(value);
            } else if (field instanceof PDRadioButton) {
                field.setValue(value);
            } else if (field instanceof PDTextField) {
                field.setValue(value);
            }
        } else {
            System.err.println("No field found with name:" + name);
        }
    }

    public static byte[] docToBytes(PDDocument document) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            document.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
