package rental.Tenant;


public class Tenant {
    private int tenantId;
    private String passportSeriesNumber;
    private String phoneNumber;
    private String fullName;

    public Tenant(int tenantId, String passportSeriesNumber, String phoneNumber, String fullName) {
        this.tenantId = tenantId;
        this.passportSeriesNumber = passportSeriesNumber;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public String getPassportSeriesNumber() { return passportSeriesNumber; }
    public void setPassportSeriesNumber(String passportSeriesNumber) { this.passportSeriesNumber = passportSeriesNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
