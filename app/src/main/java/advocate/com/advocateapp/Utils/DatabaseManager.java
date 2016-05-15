package advocate.com.advocateapp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.advocate.database.ClientTable;
import com.advocate.database.ClientTableDao;
import com.advocate.database.DaoMaster;
import com.advocate.database.DaoSession;
import com.advocate.database.FirmTable;
import com.advocate.database.FirmTableDao;
import com.advocate.database.UserTable;
import com.advocate.database.UserTableDao;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by rakesh on 9/10/15.
 */
public class DatabaseManager implements AsyncOperationListener {
    //    **
    //            * Class tag. Used for debug.
    //    */
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(this.context, "sample-database", null);
        //        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseManager(context);
        }

        return instance;
    }


    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }


    public void dropDatabase() {


    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }



    public synchronized List<UserTable> getUserByLawyerId(String lId) {
        List<UserTable> storeList = null;
        try {
            openReadableDb();

            UserTableDao dao = daoSession.getUserTableDao();
            WhereCondition condition = UserTableDao.Properties.LawyerId.eq(lId);


            QueryBuilder<UserTable> queryBuilder = dao.queryBuilder().where(condition);

            storeList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeList;
    }

    public synchronized List<FirmTable> getFirmsByLawyerId(String lawyerIds) {
        List<FirmTable> firmList = null;
        try {
            openReadableDb();

            FirmTableDao dao = daoSession.getFirmTableDao();
            WhereCondition condition = FirmTableDao.Properties.LawyerIds.like("%" + lawyerIds + "%");


            QueryBuilder<FirmTable> queryBuilder = dao.queryBuilder().where(condition);

            firmList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmList;
    }
    public synchronized List<UserTable> getLawyerByFirmId(String firmName) {
          List<UserTable> firmList = null;
          try {
              openReadableDb();

              UserTableDao dao = daoSession.getUserTableDao();
              WhereCondition condition = UserTableDao.Properties.FirmNames.like("%" + firmName + "%");


              QueryBuilder<UserTable> queryBuilder = dao.queryBuilder().where(condition);

              firmList = queryBuilder.list();
              daoSession.clear();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return firmList;
      }

    public synchronized List<UserTable> getAllLawyers() {
          List<UserTable> firmList = null;
          try {
              openReadableDb();

              UserTableDao dao = daoSession.getUserTableDao();
              WhereCondition condition = UserTableDao.Properties.LawyerId.isNotNull();


              QueryBuilder<UserTable> queryBuilder = dao.queryBuilder().where(condition);

              firmList = queryBuilder.list();
              daoSession.clear();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return firmList;
      }

    public synchronized List<FirmTable> getLawyersByFirmIdAndLawyerId(String firmName,String lawyerId) {
        List<FirmTable> firmList = null;
        try {
            openReadableDb();

            FirmTableDao dao = daoSession.getFirmTableDao();

            WhereCondition condition = FirmTableDao.Properties.FirmName.eq(firmName);
            WhereCondition condition1 = FirmTableDao.Properties.LawyerIds.like("%" + lawyerId + "%");


            QueryBuilder<FirmTable> queryBuilder = dao.queryBuilder().where(condition,condition1);

            firmList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmList;
    }


    public synchronized List<ClientTable> getClientByLawyerIdAndDate(String lawyerId,Date date) {
        List<ClientTable> clientList = null;
        try {
            openReadableDb();

            ClientTableDao dao = daoSession.getClientTableDao();
            WhereCondition condition = ClientTableDao.Properties.LawyerId.eq(lawyerId);
            WhereCondition condition1 = ClientTableDao.Properties.NextDate.eq(date);



            QueryBuilder<ClientTable> queryBuilder = dao.queryBuilder().where(condition,condition1);

            clientList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientList;
    }
    public synchronized List<ClientTable> getClientCaseNumber(String caseNumber) {
        List<ClientTable> clientList = null;
        try {
            openReadableDb();

            ClientTableDao dao = daoSession.getClientTableDao();
            WhereCondition condition = ClientTableDao.Properties.CaseNumber.eq(caseNumber);



            QueryBuilder<ClientTable> queryBuilder = dao.queryBuilder().where(condition);

            clientList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientList;
    }
    public synchronized List<FirmTable> getFirmByName(String firmName) {
        List<FirmTable> firmList = null;
        try {
            openReadableDb();

            FirmTableDao dao = daoSession.getFirmTableDao();
            WhereCondition condition = FirmTableDao.Properties.FirmName.eq(firmName);



            QueryBuilder<FirmTable> queryBuilder = dao.queryBuilder().where(condition);

            firmList = queryBuilder.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmList;
    }

    public synchronized void insertFirm(FirmTable firm) {


        try {
            if (firm != null) {
                openWritableDb();
                FirmTableDao firmDao = daoSession.getFirmTableDao();
               // for (PropertyMaster pm : propertyMasters) {
                firmDao.insertOrReplaceInTx(firm);
              //  }
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertUser(UserTable user) {


        try {
            if (user != null) {
                openWritableDb();
                UserTableDao userDao = daoSession.getUserTableDao();
               // for (PropertyMaster pm : propertyMasters) {
                userDao.insertOrReplaceInTx(user);
              //  }

                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void insertClient(ClientTable client) {


        try {
            if (client != null) {
                openWritableDb();
                ClientTableDao clientDao = daoSession.getClientTableDao();
               // for (PropertyMaster pm : propertyMasters) {
                clientDao.insertOrReplaceInTx(client);
              //  }

                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {

    }
}
