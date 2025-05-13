package rental.Organization;


import java.util.List;
import java.util.Optional;

public interface OrganizationDAO {
    List<Organization> findAll();
    Optional<Organization> findById(int organizationId);
    void add(Organization organization);
    void update(Organization organization);
    void delete(int organizationId);
}
