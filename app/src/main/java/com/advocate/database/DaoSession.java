package com.advocate.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.advocate.database.UserTable;
import com.advocate.database.ClientTable;
import com.advocate.database.FirmTable;

import com.advocate.database.UserTableDao;
import com.advocate.database.ClientTableDao;
import com.advocate.database.FirmTableDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userTableDaoConfig;
    private final DaoConfig clientTableDaoConfig;
    private final DaoConfig firmTableDaoConfig;

    private final UserTableDao userTableDao;
    private final ClientTableDao clientTableDao;
    private final FirmTableDao firmTableDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userTableDaoConfig = daoConfigMap.get(UserTableDao.class).clone();
        userTableDaoConfig.initIdentityScope(type);

        clientTableDaoConfig = daoConfigMap.get(ClientTableDao.class).clone();
        clientTableDaoConfig.initIdentityScope(type);

        firmTableDaoConfig = daoConfigMap.get(FirmTableDao.class).clone();
        firmTableDaoConfig.initIdentityScope(type);

        userTableDao = new UserTableDao(userTableDaoConfig, this);
        clientTableDao = new ClientTableDao(clientTableDaoConfig, this);
        firmTableDao = new FirmTableDao(firmTableDaoConfig, this);

        registerDao(UserTable.class, userTableDao);
        registerDao(ClientTable.class, clientTableDao);
        registerDao(FirmTable.class, firmTableDao);
    }
    
    public void clear() {
        userTableDaoConfig.getIdentityScope().clear();
        clientTableDaoConfig.getIdentityScope().clear();
        firmTableDaoConfig.getIdentityScope().clear();
    }

    public UserTableDao getUserTableDao() {
        return userTableDao;
    }

    public ClientTableDao getClientTableDao() {
        return clientTableDao;
    }

    public FirmTableDao getFirmTableDao() {
        return firmTableDao;
    }

}