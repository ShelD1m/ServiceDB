package rental.LeaseAgreement;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaseAgreementDAOImpl implements LeaseAgreementDAO {
    private final Connection connection;

    public LeaseAgreementDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<LeaseAgreement> findAll() {
        List<LeaseAgreement> list = new ArrayList<>();
        String sql = "SELECT * FROM rental.lease_agreement";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new LeaseAgreement(
                        rs.getString("agreement_number"),
                        rs.getInt("landlord_id"),
                        rs.getInt("tenant_id"),
                        rs.getInt("apartment_id"),
                        rs.getDate("signing_date"),
                        rs.getInt("term_months") // Предполагаем, что "Срок" хранится как количество месяцев (int)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(LeaseAgreement agreement) {
        // Номер договора (PK) должен быть предоставлен
        String sql = "INSERT INTO rental.lease_agreement (agreement_number, landlord_id, tenant_id, apartment_id, signing_date, term_months) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreement.getAgreementNumber());
            stmt.setInt(2, agreement.getLandlordId());
            stmt.setInt(3, agreement.getTenantId());
            stmt.setInt(4, agreement.getApartmentId());
            stmt.setDate(5, agreement.getSigningDate());
            stmt.setInt(6, agreement.getTermMonths());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LeaseAgreement agreement) {
        String sql = "UPDATE rental.lease_agreement SET landlord_id = ?, tenant_id = ?, apartment_id = ?, signing_date = ?, term_months = ? WHERE agreement_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agreement.getLandlordId());
            stmt.setInt(2, agreement.getTenantId());
            stmt.setInt(3, agreement.getApartmentId());
            stmt.setDate(4, agreement.getSigningDate());
            stmt.setInt(5, agreement.getTermMonths());
            stmt.setString(6, agreement.getAgreementNumber()); // WHERE clause
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String agreementNumber) {
        String sql = "DELETE FROM rental.lease_agreement WHERE agreement_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
