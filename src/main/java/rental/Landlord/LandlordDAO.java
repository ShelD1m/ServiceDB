package rental.Landlord;


import java.util.List;

public interface LandlordDAO {
    List<Landlord> findAll();
    void add(Landlord landlord);
    void update(Landlord landlord);
    void delete(int landlordId);
}