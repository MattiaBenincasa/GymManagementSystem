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

class WeightRoomMembershipDAOTest {
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
            WeightRoomMembership created = wrMembershipDAO.createWeightRoomMembership(wrMembership);
            wrMembershipDAO.getWeightRoomMembershipByID(created.getId());
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
        wrMembership = wrMembershipDAO.createWeightRoomMembership(wrMembership);

        wrMembership = wrMembershipDAO.getWeightRoomMembershipByID(wrMembership.getId());
        wrMembership.setName("WRUpdated");
        wrMembership.setDescription("Descrizione Updated");
        wrMembership.setDurationInDays(90);
        wrMembership.setPrice(new BigDecimal("120.00"));
        wrMembership.setType(WRMembershipType.PERSONAL);

        wrMembershipDAO.updateWeightRoomMembership(wrMembership);

        WeightRoomMembership retrieved = wrMembershipDAO.getWeightRoomMembershipByID(wrMembership.getId());
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
        wrMembership = wrMembershipDAO.createWeightRoomMembership(wrMembership);
        int id = wrMembership.getId();

        wrMembershipDAO.deleteWRMembership(id);

        assertThrows(DAOException.class, () -> {
            wrMembershipDAO.getWeightRoomMembershipByID(id);
        });
    }
}