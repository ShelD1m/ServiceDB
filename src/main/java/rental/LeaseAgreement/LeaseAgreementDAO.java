package rental.LeaseAgreement;


import java.util.List;

public interface LeaseAgreementDAO {
    List<LeaseAgreement> findAll();
    void add(LeaseAgreement agreement);
    void update(LeaseAgreement agreement);
    void delete(String agreementNumber);
}
