package rental.LeaseAgreement;


import java.sql.Date;

public class LeaseAgreement {
    private String agreementNumber;
    private int landlordId;
    private int tenantId;
    private int apartmentId;
    private Date signingDate;
    private String contractDuration;

    public LeaseAgreement(String agreementNumber, int landlordId, int tenantId, int apartmentId, Date signingDate, String contractDuration) {
        this.agreementNumber = agreementNumber;
        this.landlordId = landlordId;
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.signingDate = signingDate;
        this.contractDuration = contractDuration;
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

    public String getTermMonths() { return contractDuration; }
    public void setTermMonths(int termMonths) { this.contractDuration= contractDuration; }
}
