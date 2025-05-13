package rental.LeaseTermination;


import java.util.Optional;

public interface LeaseTerminationDAO {
    Optional<LeaseTermination> findByAgreementNumber(String agreementNumber);
    void add(LeaseTermination termination);
    void update(LeaseTermination termination);
    void delete(String agreementNumber);
    LeaseTermination findAll();
}