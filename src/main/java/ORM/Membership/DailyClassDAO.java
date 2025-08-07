package ORM.Membership;

import DomainModel.DailyClass;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DailyClassDAO {
    private final Connection connection;


    public DailyClassDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public DailyClass createDailyClass(DailyClass dailyClass) {}

    public void deleteDailyClass(int dailyClassID) {}


    //set isActive to False
    public void cancelDailyClass(int dailyClassID) {}

    public DailyClass getDailyClassByID(int dailyClassID) {}

    public ArrayList<DailyClass> getAllDailyClasses() {}

    public ArrayList<DailyClass> getAllDailyClassesByCourse(int courseID) {}

}
