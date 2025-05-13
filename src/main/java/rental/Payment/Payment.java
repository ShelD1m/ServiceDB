package rental.Payment;


import java.sql.Date;

public class Payment {
    private int paymentId;
    private int tenantId;
    private int serviceCode;
    private int organizationId;
    private Date paymentDate;

    public Payment(int paymentId, int tenantId, int serviceCode, int organizationId, Date paymentDate) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.serviceCode = serviceCode;
        this.organizationId = organizationId;
        this.paymentDate = paymentDate;
    }


    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getServiceCode() { return serviceCode; }
    public void setServiceCode(int serviceCode) { this.serviceCode = serviceCode; }

    public int getOrganizationId() { return organizationId; }
    public void setOrganizationId(int organizationId) { this.organizationId = organizationId; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
}
