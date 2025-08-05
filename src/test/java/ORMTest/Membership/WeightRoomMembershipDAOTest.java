package ORMTest.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import ORM.Membership.MembershipDAO;
import ORM.Membership.WeightRoomMembershipDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class WRMembershipDAOTest {
    private WeightRoomMembershipDAO wrMembershipDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        wrMembershipDAO = new WeightRoomMembershipDAO(new MembershipDAO());
    }

    @Test
    void testCreateWRMembership() throws DAOException {
        WeightRoomMembership wrMembership = new WeightRoomMembership();
        wrMembership.setName("Abbonamento");
        wrMembership.setDescription("Abbonamento per sala pesi");
        wrMembership.setPrice(new BigDecimal("99.99"));
        wrMembership.setDurationInDays(90);
        wrMembership.setType(WRMembershipType.BASE);
        assertDoesNotThrow(()->{
            int id = wrMembershipDAO.createWeightRoomMembership(wrMembership);
            wrMembershipDAO.getWeightRoomMembershipByID(id);
        });
    }

    @Test
    void testUpdateWRMembership() throws DAOException {
        WeightRoomMembership wrMembership = new WeightRoomMembership();
        wrMembership.setName("Abbonamento");
        wrMembership.setDescription("Abbonamento per sala pesi");
        wrMembership.setPrice(new BigDecimal("99.99"));
        wrMembership.setDurationInDays(90);
        wrMembership.setType(WRMembershipType.BASE);
        int id = wrMembershipDAO.createWeightRoomMembership(wrMembership);

        wrMembership = wrMembershipDAO.getWeightRoomMembershipByID(id);
        wrMembership.setName("WRUpdated");
        wrMembership.setDescription("Descrizione Updated");
        wrMembership.setDurationInDays(90);
        wrMembership.setPrice(new BigDecimal("120.00"));
        wrMembership.setType(WRMembershipType.PERSONAL);

        wrMembershipDAO.updateWeightRoomMembership(wrMembership);

        WeightRoomMembership retrieved = wrMembershipDAO.getWeightRoomMembershipByID(id);
        assertEquals("WRUpdated", retrieved.getName());
        assertEquals(WRMembershipType.PERSONAL, retrieved.getType());
        assertEquals("Descrizione Updated", retrieved.getDescription());
        assertEquals(90, retrieved.getDurationInDays());
        assertEquals(new BigDecimal("120.00"), retrieved.getPrice());
    }

    @Test
    void testDeleteWRMembership() {
        WeightRoomMembership wrMembership = new WeightRoomMembership();
        wrMembership.setName("Abbonamento");
        wrMembership.setDescription("Abbonamento per sala pesi");
        wrMembership.setPrice(new BigDecimal("99.99"));
        wrMembership.setDurationInDays(90);
        wrMembership.setType(WRMembershipType.BASE);
        int id = wrMembershipDAO.createWeightRoomMembership(wrMembership);

        wrMembershipDAO.deleteWRMembership(id);

        assertThrows(DAOException.class, () -> {
            wrMembershipDAO.getWeightRoomMembershipByID(id);
        });
    }
}