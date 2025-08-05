package ORMTest.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.CustomerFee;
import DomainModel.RegistrationFee;
import DomainModel.Users.Customer;
import ORM.Users.CustomerDAO;
import ORM.Membership.CustomerFeeDAO;
import ORM.Membership.RegistrationFeeDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationFeeAndCustomerFeeDAOTest {
    private RegistrationFeeDAO registrationFeeDAO;
    private CustomerFeeDAO customerFeeDAO;
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        registrationFeeDAO = new RegistrationFeeDAO();
        customerFeeDAO = new CustomerFeeDAO(userDAO);
        customerDAO = new CustomerDAO(userDAO);
    }

    @Test
    void test1_createRegistrationFee_shouldSucceed() {
        RegistrationFee fee = RegistrationFee.getRegistrationFee();
        fee.setPrice(new BigDecimal("25.00"));
        fee.setDescription("Quota di iscrizione annuale");

        assertDoesNotThrow(() -> {
            registrationFeeDAO.createRegistrationFee(fee);
            Optional<RegistrationFee> retrievedFee = registrationFeeDAO.getRegistrationFee();
            assertTrue(retrievedFee.isPresent());
            assertEquals(new BigDecimal("25.00"), retrievedFee.get().getPrice());
        });
    }

    @Test
    void test3_updateRegistrationFee_shouldUpdateFields() {

        RegistrationFee originalFee = RegistrationFee.getRegistrationFee();
        originalFee.setPrice(new BigDecimal("25.00"));
        originalFee.setDescription("Quota di iscrizione annuale");
        int id = registrationFeeDAO.createRegistrationFee(originalFee);

        originalFee.setPrice(new BigDecimal("30.00"));
        originalFee.setDescription("Quota aggiornata");

        registrationFeeDAO.updateRegistrationFee(originalFee);

        Optional<RegistrationFee> retrievedFee = registrationFeeDAO.getRegistrationFee();
        assertTrue(retrievedFee.isPresent());
        assertEquals(new BigDecimal("30.00"), retrievedFee.get().getPrice());
        assertEquals("Quota aggiornata", retrievedFee.get().getDescription());
    }

    @Test
    void test4_assignCustomerFee_shouldCreateRecord() throws DAOException {
        // Creiamo un cliente di prova
        Customer customer = UserDAOTestUtils.createCustomer("mario.rossi", "password");
        int customerId = customerDAO.createCustomer(customer);
        customer = customerDAO.getCustomerByID(customerId);
        // Creiamo un CustomerFee
        CustomerFee customerFee = new CustomerFee(LocalDate.now(), customer);

        assertDoesNotThrow(() -> {
            int feeId = customerFeeDAO.createCustomerFee(customerFee);
            CustomerFee retrievedFee = customerFeeDAO.getCustomerFeeByID(feeId);
            assertNotNull(retrievedFee);
            assertEquals(customerId, retrievedFee.getCustomer().getId());
            assertEquals(LocalDate.now(), retrievedFee.getStartDate());
            assertEquals(LocalDate.now().plusYears(1), retrievedFee.getExpiryDate());
        });
    }
}
