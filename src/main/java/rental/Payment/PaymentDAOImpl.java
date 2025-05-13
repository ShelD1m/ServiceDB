package rental.Payment;


import rental.Apartment.Apartment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    private final Connection connection;

    public PaymentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /*@Override
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        // Столбцы: payment_id, tenant_id, service_code, organization_id, payment_date
        String sql = "SELECT * FROM rental.payment";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }*/

    @Override
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rental.payment")) {
            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("tenant_id"),
                        rs.getInt("service_code"),
                        rs.getInt("organization_id"),
                        rs.getDate("payment_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Payment> findByTenant(int tenantId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM rental.payment WHERE tenant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tenantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToPayment(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void add(Payment payment) {
        String sql = "INSERT INTO rental.payment (tenant_id, service_code, organization_id, payment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, payment.getTenantId());
            stmt.setInt(2, payment.getServiceCode());
            stmt.setInt(3, payment.getOrganizationId());
            stmt.setDate(4, payment.getPaymentDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Payment payment) {
        String sql = "UPDATE rental.payment SET tenant_id = ?, service_code = ?, organization_id = ?, payment_date = ? WHERE payment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, payment.getTenantId());
            stmt.setInt(2, payment.getServiceCode());
            stmt.setInt(3, payment.getOrganizationId());
            stmt.setDate(4, payment.getPaymentDate());
            stmt.setInt(5, payment.getPaymentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int paymentId) {
        String sql = "DELETE FROM rental.payment WHERE payment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Payment mapRowToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getInt("tenant_id"),
                rs.getInt("service_code"),
                rs.getInt("organization_id"),
                rs.getDate("payment_date")
        );
    }
}