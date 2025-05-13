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
        String sql = "SELECT * FROM rental.sign_contract";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new LeaseAgreement(
                        rs.getString("contract_id"),
                        rs.getInt("landlord_id"),
                        rs.getInt("tenant_id"),
                        rs.getInt("apartment_id"),
                        rs.getDate("contract_date"),
                        rs.getString("contract_duration")
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
        String sql = "INSERT INTO rental.sign_contract (agreement_number, landlord_id, tenant_id, apartment_id, signing_date, term_months) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreement.getAgreementNumber());
            stmt.setInt(2, agreement.getLandlordId());
            stmt.setInt(3, agreement.getTenantId());
            stmt.setInt(4, agreement.getApartmentId());
            stmt.setDate(5, agreement.getSigningDate());
            stmt.setString(6, agreement.getTermMonths());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LeaseAgreement agreement) {
        String sql = "UPDATE rental.sign_contract SET landlord_id = ?, tenant_id = ?, apartment_id = ?, signing_date = ?, term_months = ? WHERE agreement_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agreement.getLandlordId());
            stmt.setInt(2, agreement.getTenantId());
            stmt.setInt(3, agreement.getApartmentId());
            stmt.setDate(4, agreement.getSigningDate());
            stmt.setString(5, agreement.getTermMonths());
            stmt.setString(6, agreement.getAgreementNumber()); // WHERE clause
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String agreementNumber) {
        String sql = "DELETE FROM rental.sign_contract WHERE agreement_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agreementNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
