package rental.Apartment;

import java.util.List;

public interface ApartmentDAO {
    List<Apartment> findAll();
    void add(Apartment apartment);
    void update(Apartment apartment);
    void delete(int apartmentId);
}