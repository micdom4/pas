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
    public User findByLogin(String login) throws UserLoginException, UserNotFoundException {
        if (login == null || login.isEmpty()) {
            throw new UserLoginException("Provided login is empty");
        }

        Bson filter = Filters.eq("login", login);

        UserEntity entity = userCollection.find(filter).first();

        if (entity == null) {
            throw new UserNotFoundException("User not found with login: " + login);
        }

        return mapper.toData(entity);

    }

    @Override
    public List<User> findByMatchingLogin(String loginStart) {
        Bson filter = Filters.regex("login", "^" + Pattern.quote(loginStart) + "(.)*");

        List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());

        return mapper.toDataList(entities);
    }

    @Override
    public <T extends User> User add(String login, String name, String surname, Class<T> userClass) throws UserTypeException, UserLoginException, UserAlreadyExistsException {
        if (login == null || login.isEmpty()) {
            throw new UserLoginException("User login may not be empty");
        }

        UserEntity.Type type;

        if (userClass.equals(Client.class)) {
            type = UserEntity.Type.CLIENT;
        } else if (userClass.equals(Manager.class)) {
            type = UserEntity.Type.MANAGER;
        } else if (userClass.equals(Admin.class)) {
            type = UserEntity.Type.ADMIN;
        } else {
            throw new UserTypeException("Invalid type");
        }

        try {
            findByLogin(login);
        } catch (UserNotFoundException e) {
            InsertOneResult result = userCollection.insertOne(new UserEntity(null, login, name, surname, type, false));

            try {
                return findByLogin(login);
            } catch (UserNotFoundException ex) {
                throw new MongoException("Error while adding new User");
            }
        }

        throw new UserAlreadyExistsException("User with login: " + login + " already exists");
    }

    @Override
    public User update(String id, String surname) throws UserNotFoundException, UserIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("surname", surname);

        UpdateResult result = userCollection.updateOne(filter, update);
        if (result.getMatchedCount() == 0) {
            throw new UserNotFoundException("No user found with ID: " + id);
        }

        return findById(id);
    }

    @Override
    public void activate(String id) throws UserNotFoundException, UserIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", true);

        UpdateResult result = userCollection.updateOne(filter, update);

        if (result.getModifiedCount() == 0) {
            throw new UserNotFoundException("No User found with ID: " + id);
        } else if (result.getModifiedCount() != 1) {
            throw new MongoException("Error while activating user by ID: " + id);
        }
    }

    @Override
    public void deactivate(String id) throws UserNotFoundException, UserIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("active", false);

        UpdateResult result = userCollection.updateOne(filter, update);

        if (result.getModifiedCount() == 0) {
            throw new UserNotFoundException("No User found with ID: " + id);
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
    public User findById(String id) throws UserIdException, UserNotFoundException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);
        UserEntity entity = userCollection.find(filter).first();

        if (entity == null) {
            throw new UserNotFoundException("No user found with ID: " + id);
        }

        return mapper.toData(entity);
    }

    private ObjectId getObjectId(String id) throws UserIdException {
        ObjectId objectId;

        try {
            objectId = idMapper.stringToObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new UserIdException("Invalid user ID: " + id);
        }

        if (objectId == null) {
            throw new UserIdException("User ID cannot be empty");
        }

        return objectId;
    }
}
