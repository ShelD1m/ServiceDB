package rental.Landlord;


public class Landlord {
    private int landlordId;
    private String passportSeriesNumber;
    private String fullName;
    private String phoneNumber;

    public Landlord(int landlordId, String passportSeriesNumber, String fullName, String phoneNumber) {
        this.landlordId = landlordId;
        this.passportSeriesNumber = passportSeriesNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }


    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public String getPassportSeriesNumber() { return passportSeriesNumber; }
    public void setPassportSeriesNumber(String passportSeriesNumber) { this.passportSeriesNumber = passportSeriesNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}