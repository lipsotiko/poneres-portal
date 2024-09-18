package io.meraklis.icare.top_flight.schedule;

import lombok.Data;

import java.util.List;

@Data
public class FlightInvoice {
    private List<FlightInvoiceItem> items;
    private int paymentId;
    private PaymentStatus paymentStatus;
}
