package rental.Service;


import java.util.List;
import java.util.Optional;

public interface ServiceDAO {
    List<Service> findAll();
    Optional<Service> findByCode(int serviceCode);
    void add(Service service);
    void update(Service service);
    void delete(int serviceCode);
}
