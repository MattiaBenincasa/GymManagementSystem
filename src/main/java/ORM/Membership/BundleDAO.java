package ORM.Membership;

import DomainModel.Membership.Bundle;
import ORM.ConnectionManager;

import java.sql.Connection;

public class BundleDAO {
    private Connection connection;

    public BundleDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public Bundle createBundle(Bundle bundle) {return null;}

    public void updateBundle(Bundle bundle) {}

    public void deleteBundle(Bundle bundle) {}

}
