package team.four.pas.repositories.implementation;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import team.four.pas.exceptions.user.*;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.data.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public User findByLogin(String login) throws UserInvalidLoginException, UserNotPresentException {
        if (login == null || login.isEmpty()) {
            throw new UserInvalidLoginException("Provided login is empty");
        }

        Bson filter = Filters.eq("login", login);

        UserEntity entity = userCollection.find(filter).first();

        if (entity == null) {
            throw new UserNotPresentException("User not found with login: " + login);
        }

        return mapper.toData(entity);

    }

    @Override
    public List<User> findByLogin(List<String> logins) throws UserInvalidLoginException {
        if (logins == null || logins.isEmpty()) {
            throw new UserInvalidLoginException("No logins provided");
        }

        Bson filter = Filters.in("login", logins);
        List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());

        return mapper.toDataList(entities);
    }

    @Override
    public List<User> findByMatchingLogin(String loginStart) {
        Bson filter = Filters.regex("login", "^" + Pattern.quote(loginStart) + "(.)*");

        List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());
        return mapper.toDataList(entities);
    }

    @Override
    public <T extends User> User add(String login, String name, String surname, Class<T> userClass) throws UserInvalidTypeException, UserInvalidLoginException, UserNotPresentException, UserAlreadyExistsException {
        if (login == null || login.isEmpty()) {
            throw new UserInvalidLoginException("Provided login is empty");
        }

        UserEntity.Type type;

        if (userClass.equals(Client.class)) {
            type = UserEntity.Type.CLIENT;
        } else if (userClass.equals(Manager.class)) {
            type = UserEntity.Type.MANAGER;
        } else if (userClass.equals(Admin.class)) {
            type = UserEntity.Type.ADMIN;
        } else {
            throw new UserInvalidTypeException("Invalid type");
        }

        try {
            findByLogin(login);
        } catch (UserNotPresentException e) {
            InsertOneResult result = userCollection.insertOne(new UserEntity(null, login, name, surname, type, false));

            return findByLogin(login);
        }

        throw new UserAlreadyExistsException("User with login: " + login + " already exists");
    }

    @Override
    public User update(String id, String surname) throws UserNotPresentException, UserInvalidLoginException, UserInvalidIdException {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) {
            throw new UserInvalidLoginException("Empty login provided");
        }

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("surname", surname);

        UpdateResult result = userCollection.updateOne(filter, update);
        if (result.getMatchedCount() == 0) {
            throw new UserNotPresentException("No user found with ID: " + id);
        }

        return findById(id);
    }

    @Override
    public void activate(String id) throws UserInvalidLoginException, UserNotPresentException {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) {
            throw new UserInvalidLoginException("Empty login provided");
        }

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", true);

        UpdateResult result = userCollection.updateOne(filter, update);

        if (result.getModifiedCount() == 0) {
            throw new UserNotPresentException("No User found with ID: " + id);
        } else if (result.getModifiedCount() != 1) {
            throw new MongoException("Error while activating user by ID: " + id);
        }
    }

    @Override
    public void deactivate(String id) throws UserInvalidLoginException, UserNotPresentException {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) {
            throw new UserInvalidLoginException("Empty login provided");
        }

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", false);

        UpdateResult result = userCollection.updateOne(filter, update);

        if (result.getModifiedCount() == 0) {
            throw new UserNotPresentException("No User found with ID: " + id);
        } else if (result.getModifiedCount() != 1) {
            throw new MongoException("Error while deactivating user by ID: " + id);
        }
    }

    @Override
    public List<User> getAll() {
        List<UserEntity> entities = userCollection.find().into(new ArrayList<>());

        return mapper.toDataList(entities);
    }

    @Override
    public User findById(String id) throws UserInvalidIdException, UserNotPresentException {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) {
            throw new UserInvalidIdException("Invalid ID:" + id);
        }

        Bson filter = Filters.eq("_id", objectId);
        UserEntity entity = userCollection.find(filter).first();

        if (entity == null) {
            throw new UserNotPresentException("No user found with ID: " + id);
        }

        return mapper.toData(entity);
    }

}
