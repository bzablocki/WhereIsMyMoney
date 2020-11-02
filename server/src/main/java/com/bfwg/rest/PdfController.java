package com.bfwg.rest;

import com.bfwg.model.Transaction;
import com.bfwg.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.xerces.dom.ElementImpl;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfController {
    private String pdfPath;
    private User user;
    private static final double DATE_START_LEFT = 52.44;
    private static final double DETAILS_START_LEFT_MIN = 113.;
    private static final double DETAILS_START_LEFT_MAX = 114.;
    private static final double NAME_MAX_LEFT = 370.;
    private static final double TRANSACTION_TYPE_MAX_LEFT = 440.;
    private static final double AMOUNT_MAX_LEFT = 550.;

    public PdfController(User user, String pdfPath) {
        this.user = user;
        this.pdfPath = pdfPath;
    }

    public List<Transaction> getTransactionsList() {
        List<Transaction> transactions;
        try (PDDocument pdfDocument = getPdfDocument()) {
            Document domDocument = getPdfDomTree(pdfDocument);
            transactions = getTransactionsListFromDom(domDocument);
        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        return transactions;

    }

    private List<Transaction> getTransactionsListFromDom(Document dom) {
        List<Transaction> transactions = new ArrayList<>();

        Element body = (Element) ((Element) dom.getElementsByTagName("html").item(0)).getElementsByTagName("body").item(0);
        NodeList divPages = body.getChildNodes();

        for (int i = 0; i < divPages.getLength(); i++) {
            Element page0 = (Element) divPages.item(i);
            if (page0.getAttribute("class").equals("page")) {
                NodeList pageElements = page0.getElementsByTagName("div");
                List<Transaction> ts = getTransactionsFromPage(pageElements);
                transactions.addAll(ts);
            }

        }

        return transactions;
    }

    private List<Transaction> getTransactionsFromPage(NodeList pageElements) {
        List<Transaction> transactions = new ArrayList<>();

        int nDivs = pageElements.getLength();
        MutableInt divPointer = new MutableInt(0);
        boolean doProcess = true;
        while (doProcess) {
            Node pageDiv = pageElements.item(divPointer.intValue());

            if (isTransactionBeginning(pageDiv)) {
                Transaction transaction = getTransactionDetails(pageElements, divPointer);
                transactions.add(transaction);
            } else {
                divPointer.increment();
            }

            if (divPointer.intValue() >= nDivs) {
                doProcess = false;
            }

        }

        return transactions;
    }

    private Transaction getTransactionDetails(NodeList pageElements, MutableInt divPointer) {
        Transaction t = new Transaction();
        t.setUser(this.user);

        LocalDate reservedDate = constructDate(pageElements, divPointer);
        t.setReservedDate(reservedDate);
        divPointer.increment();

        String transactionName = constructTransactionName(pageElements, divPointer);
        t.setName(transactionName);
        divPointer.increment();

        String transactionType = getField(pageElements, divPointer, TRANSACTION_TYPE_MAX_LEFT);
        t.setType(transactionType);
        divPointer.increment();

        Double transactionAmount = constructAmount(pageElements, divPointer);
        t.setAmount(transactionAmount);
        divPointer.increment();

        while (isAdditionalField(pageElements, divPointer)) {
            String additionalField = getField(pageElements, divPointer, NAME_MAX_LEFT);
            fillTransactionWithAdditionalField(t, additionalField);
            divPointer.increment();
        }

        return t;
    }

    private LocalDate constructDate(NodeList pageElements, MutableInt divPointer) {
        String reservedDateString = pageElements.item(divPointer.intValue()).getTextContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(reservedDateString, formatter);
    }

    private void fillTransactionWithAdditionalField(Transaction t, String additionalField) {
        if (additionalField.startsWith(AdditionalFieldEnum.CARD_SEQ_NB.getStartsWith())) {
            t.setCardSequenceNo(additionalField);
        } else if (additionalField.startsWith(AdditionalFieldEnum.TRANSACTION.getStartsWith())) {
            t.setTransactionField(additionalField);
        } else if (additionalField.startsWith(AdditionalFieldEnum.RESERVATION.getStartsWith())) {
            t.setReservation(true);
        } else if (additionalField.startsWith(AdditionalFieldEnum.IBAN.getStartsWith())) {
            t.setIban(additionalField);
        } else if (additionalField.startsWith(AdditionalFieldEnum.VALUE_DATE.getStartsWith())) {
            t.setValueDate(additionalField);
        } else if (additionalField.startsWith(AdditionalFieldEnum.DATE_TIME.getStartsWith())) {
            t.setDateTime(additionalField);
        }

    }

    private boolean isAdditionalField(NodeList pageElements, MutableInt divPointer) {
        String style = getStyle(pageElements.item(divPointer.intValue()));
        double left = getStylePropertyValue(style, StyleKeyEnum.LEFT);
        return left >= DETAILS_START_LEFT_MIN && left <= DETAILS_START_LEFT_MAX;
    }

    private Double constructAmount(NodeList pageElements, MutableInt divPointer) {
        String amount = getField(pageElements, divPointer, AMOUNT_MAX_LEFT);
        amount = amount.replaceAll("\\.", "")
                .replaceAll(",", ".")
                .replaceAll(" ", "")
                .replaceAll("\\+", "");
        return Double.parseDouble(amount);
    }

    private String constructTransactionName(NodeList pageElements, MutableInt divPointer) {
        return getField(pageElements, divPointer, NAME_MAX_LEFT);
    }

    private String getField(NodeList pageElements, MutableInt divPointer, double maxLeft) {
        double requiredTop = getStylePropertyValue(getStyle(pageElements.item(divPointer.intValue())), StyleKeyEnum.TOP);
        StringBuilder transactionName = new StringBuilder();
        while (isSameField(pageElements.item(divPointer.intValue()), maxLeft, requiredTop)) {
            String name = pageElements.item(divPointer.intValue()).getTextContent();
            if (!name.equals("")) {
//            if (!name.equals("&nbsp;")){
                transactionName.append(name);
                transactionName.append(" ");
            }
            divPointer.increment();
        }
        divPointer.decrement();
        return transactionName.toString();
    }

    private boolean isSameField(Node item, double maxLeft, double reqTop) {
        if (item.getTextContent().equals("")) {
            return true;
        }
        String style = getStyle(item);
        double left = getStylePropertyValue(style, StyleKeyEnum.LEFT);
        double top = getStylePropertyValue(style, StyleKeyEnum.TOP);
        return left < maxLeft && top == reqTop;
    }


    private boolean isTransactionBeginning(Node pageDiv) {
        String style = getStyle(pageDiv);
        double left = getStylePropertyValue(style, StyleKeyEnum.LEFT);
        return left == DATE_START_LEFT && isDateFormat(pageDiv.getTextContent());
    }

    private boolean isDateFormat(String text) {
        Pattern p = Pattern.compile("([0-9]*-[0-9]*-[0-9]*)");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    private double getStylePropertyValue(String style, StyleKeyEnum styleKey) {
        String keyString = styleKey.getKey();
        String strValue = StringUtils.substringBetween(style, keyString + ":", "pt;");
        if (strValue == null) {
            return -1.;
        }
        return Double.parseDouble(strValue);
    }

    private String getStyle(Node pageDiv) {
        return ((ElementImpl) pageDiv).getAttribute("style");
    }


    private Document getPdfDomTree(PDDocument pdfDocument) throws IOException, ParserConfigurationException {
        PDFDomTree parser = new PDFDomTree();
        return parser.createDOM(pdfDocument);
    }

    private PDDocument getPdfDocument() throws IOException {
        return PDDocument.load(new File(this.pdfPath));
    }

}
