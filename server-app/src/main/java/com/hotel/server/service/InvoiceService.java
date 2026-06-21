package com.hotel.server.service;

import com.hotel.server.dao.InvoiceDAO;
import com.hotel.server.model.Invoice;
import com.hotel.server.util.LogUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InvoiceService {
    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    /**
     * Get invoice by ID
     */
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDAO.findById(invoiceId);
    }

    /**
     * Get all invoices
     */
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.findAll();
    }

    /**
     * Get invoice by booking ID
     */
    public Invoice getInvoiceByBookingId(int bookingId) {
        return invoiceDAO.findByBookingId(bookingId);
    }

    /**
     * Create new invoice
     */
    public int createInvoice(Invoice invoice) {
        try {
            // Generate invoice number
            String invoiceNumber = "INV-" + System.currentTimeMillis();
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setInvoiceDate(LocalDateTime.now());

            // Calculate tax amount
            BigDecimal subtotal = invoice.getRoomCharges().add(invoice.getServiceCharges())
                    .subtract(invoice.getDiscount());
            BigDecimal tax = subtotal.multiply(invoice.getTaxRate()).divide(new BigDecimal(100));
            invoice.setTaxAmount(tax);
            invoice.setRemainingAmount(invoice.getTotalAmount());

            int invoiceId = invoiceDAO.insert(invoice);
            if (invoiceId > 0) {
                LogUtil.logActivity(invoice.getCreatedBy(), LogUtil.ActionType.CREATE,
                        LogUtil.ModuleType.BOOKING, invoiceId, "Invoice created", null, null);
                return invoiceId;
            }
        } catch (Exception e) {
            LogUtil.logError("Error creating invoice", e);
        }
        return -1;
    }

    /**
     * Update invoice payment
     */
    public boolean updateInvoicePayment(int invoiceId, BigDecimal paidAmount, String paymentMethod) {
        Invoice invoice = invoiceDAO.findById(invoiceId);
        if (invoice == null) {
            return false;
        }

        invoice.setPaidAmount(paidAmount);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(LocalDateTime.now());

        BigDecimal remaining = invoice.getTotalAmount().subtract(paidAmount);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setPaymentStatus("PAID");
            invoice.setRemainingAmount(BigDecimal.ZERO);
        } else {
            invoice.setPaymentStatus("PARTIAL");
            invoice.setRemainingAmount(remaining);
        }

        boolean result = invoiceDAO.update(invoice);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    invoiceId, "Invoice payment updated", null, null);
        }
        return result;
    }

    /**
     * Search invoices
     */
    public List<Invoice> searchInvoices(String keyword) {
        return invoiceDAO.search(keyword);
    }
}
