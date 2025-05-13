package rental.LeaseAgreement;


import java.sql.Date;

public class LeaseAgreement {
    private String agreementNumber;
    private int landlordId;
    private int tenantId;
    private int apartmentId;
    private Date signingDate;
    private int termMonths;

    public LeaseAgreement(String agreementNumber, int landlordId, int tenantId, int apartmentId, Date signingDate, int termMonths) {
        this.agreementNumber = agreementNumber;
        this.landlordId = landlordId;
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.signingDate = signingDate;
        this.termMonths = termMonths;
    }

    public String getAgreementNumber() { return agreementNumber; }
    public void setAgreementNumber(String agreementNumber) { this.agreementNumber = agreementNumber; }

    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getApartmentId() { return apartmentId; }
    public void setApartmentId(int apartmentId) { this.apartmentId = apartmentId; }

    public Date getSigningDate() { return signingDate; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }

    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
}
