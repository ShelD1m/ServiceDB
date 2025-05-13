package rental.Tenant;


import java.util.List;

public interface TenantDAO {
    List<Tenant> findAll();
    void add(Tenant tenant);
    void update(Tenant tenant);
    void delete(int tenantId);
}