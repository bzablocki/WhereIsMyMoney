package com.bfwg.rest;

import lombok.Getter;

public enum AdditionalFieldEnum {
    CARD_SEQ_NB("Card sequence no"),
    TRANSACTION("Transaction"),
    RESERVATION("*** Reservation ***"),
    IBAN("IBAN"),
    REFERENCE("Reference"),
    VALUE_DATE("Value date"),
    DATE_TIME("Date/time");


    @Getter
    private String startsWith;

    AdditionalFieldEnum(String startsWith) {
        this.startsWith = startsWith;
    }

}
