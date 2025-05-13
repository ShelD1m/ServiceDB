package rental.Address;


import java.util.List;
import java.util.Optional;

public interface AddressDAO {
    List<Address> findAll();
    Optional<Address> findByApartmentId(int apartmentId);
    void add(Address address);
    void update(Address address);
    void delete(int apartmentId);
}