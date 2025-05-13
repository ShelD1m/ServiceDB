package rental.LeaseTermination;


import java.sql.Date;

public class LeaseTermination {
    private String agreementNumber;
    private String reason;
    private Date terminationDate;

    public LeaseTermination(String agreementNumber, String reason, Date terminationDate) {
        this.agreementNumber = agreementNumber;
        this.reason = reason;
        this.terminationDate = terminationDate;
    }

    public String getAgreementNumber() { return agreementNumber; }
    public void setAgreementNumber(String agreementNumber) { this.agreementNumber = agreementNumber; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Date getTerminationDate() { return terminationDate; }
    public void setTerminationDate(Date terminationDate) { this.terminationDate = terminationDate; }
}
