package team.four.pas.repositories.implementation;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.data.users.User;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MongoUserRepository implements UserRepository {
    private final MongoCollection<UserEntity> userCollection;
    private final UserMapper mapper;
    private final StringToObjectId idMapper;

    public MongoUserRepository(MongoCollection<UserEntity> userCollection, UserMapper mapper, StringToObjectId idMapper) {
        this.userCollection = userCollection;
        this.mapper = mapper;
        this.idMapper = idMapper;
    }

    @Override
    public User findByLogin(String login) {
        if (login == null || login.isEmpty()) {
            return null;
        }

        Bson filter = Filters.eq("login", login);
        try {
            UserEntity entity = userCollection.find(filter).first();
            return mapper.toData(entity);
        } catch (MongoException e) {
            System.err.println("Error finding user by login: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> findByLogin(List<String> logins) {
        if (logins == null || logins.isEmpty()) {
            return Collections.emptyList();
        }

        Bson filter = Filters.in("login", logins);
        try {
            List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());
            return mapper.toDataList(entities);
        } catch (MongoException e) {
            System.err.println("Error finding users by login list: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<User> findByMatchingLogin(String loginStart) {
        Bson filter = Filters.regex("login", "^" + Pattern.quote(loginStart) + "(.)*");

        try {
            List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());
            return mapper.toDataList(entities);
        } catch (MongoException e) {
            System.err.println("Error finding users by matching login: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public <T extends User> User add(String login, String name, String surname, Class<T> userClass) throws ServerException,
                                                                                                           KeyManagementException,
                                                                                                           BadAttributeValueExpException {
        UserEntity.Type type;

        if (userClass.equals(Client.class)) {
            type = UserEntity.Type.CLIENT;
        } else if (userClass.equals(Manager.class)) {
            type = UserEntity.Type.MANAGER;
        } else if (userClass.equals(Admin.class)) {
            type = UserEntity.Type.ADMIN;
        } else {
            throw new BadAttributeValueExpException("Invalid type");
        }

        try {
            InsertOneResult result = userCollection.insertOne( new UserEntity(null, login, name, surname, type, false));
            return findByLogin(login);
        }
        catch (MongoException e) {
            if(e.getCode() == 11000 || e.getCode() == 121) {
                throw new KeyManagementException(e.getMessage());
            }
            throw new ServerException("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public User update(String id, String surname) {
        ObjectId objectId = idMapper.stringToObjectId(id);

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("surname", surname);
        try {
            UpdateResult result = userCollection.updateOne(filter, update);
            if (result.getMatchedCount() != 1) {
                //exception
            }
            return findById(id);
        } catch (MongoException e) {
            System.err.println("Error updating user by ID: " + e.getMessage());
            //exception
            return null;
        }
    }

    @Override
    public boolean activate(String id) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return false;

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", true);
        try {
            UpdateResult result = userCollection.updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error activating user by ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deactivate(String id) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return false;

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", false);
        try {
            UpdateResult result = userCollection.updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error deactivating user by ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAll() {
        try {
            List<UserEntity> entities = userCollection.find().into(new ArrayList<>());
            return mapper.toDataList(entities);
        } catch (MongoException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public User findById(String id) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return null;

        Bson filter = Filters.eq("_id", objectId);
        try {
            UserEntity entity = userCollection.find(filter).first();
            return mapper.toData(entity);
        } catch (MongoException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            return null;
        }
    }

}
