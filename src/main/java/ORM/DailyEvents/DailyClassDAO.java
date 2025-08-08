package ORM.DailyEvents;

import DomainModel.DailyEvents.DailyClass;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public class DailyClassDAO {
    private final Connection connection;


    public DailyClassDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public DailyClass createDailyClass(DailyClass dailyClass) {return null;}

    public void deleteDailyClass(int dailyClassID) {}


    //set isActive to False
    public void cancelDailyClass(int dailyClassID) {}

    public DailyClass getDailyClassByID(int dailyClassID) {return null;}

    public ArrayList<DailyClass> getAllDailyClasses() {return null;}

    public ArrayList<DailyClass> getAllDailyClassesByCourse(int courseID) {return null;}

}
