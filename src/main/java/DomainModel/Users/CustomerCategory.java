package DomainModel.Users;

import java.time.LocalDate;
import java.time.Period;

// A customer can belong to only one of these categories
public enum CustomerCategory {
    STUDENT {
        @Override
        public boolean isValidFor(Customer customer) {
            int age = Period.between(customer.getBirthDate(), LocalDate.now()).getYears();
            return age <= 25;
        }
    },
    SENIOR {
        @Override
        public boolean isValidFor(Customer customer) {
            int age = Period.between(customer.getBirthDate(), LocalDate.now()).getYears();
            return age > 60;
        }
    },
    MOTHER {
        @Override
        public boolean isValidFor(Customer customer) {
            return true;
        }
    },
    MILITARY {
        @Override
        public boolean isValidFor(Customer customer) {
            return true;
        }
    },
    GYM_STAFF {
        @Override
        public boolean isValidFor(Customer customer) {
            return true;
        }
    };
    //...

    public abstract boolean isValidFor(Customer customer);
}

