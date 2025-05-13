package rental.Payment;


import java.util.List;

public interface PaymentDAO {
    List<Payment> findAll();
    List<Payment> findByTenant(int tenantId);
    void add(Payment payment);
    void update(Payment payment);
    void delete(int paymentId);
}
