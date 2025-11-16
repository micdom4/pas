package team.four.pas.repositories.implementation;

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
    public <T extends User> boolean add(String login, String name, String surname, Class<T> userClass) {
        if(login.isEmpty() || findByLogin(login) != null) {
            System.err.println("Invalid login:" + login);
            return false;
        }

        UserEntity.Type type;

        if (userClass.equals(Client.class)) {
            type = UserEntity.Type.CLIENT;
        } else if (userClass.equals(Manager.class)) {
            type = UserEntity.Type.MANAGER;
        } else if (userClass.equals(Admin.class)) {
            type = UserEntity.Type.ADMIN;
        } else {
            System.err.println("Unknown user class: " + userClass.getName());
            return false;
        }


        try {
            InsertOneResult result = userCollection.insertOne( new UserEntity(null, login, name, surname, type));
            return result.wasAcknowledged();
        } catch (MongoException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(String id, String surname) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return false;

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("surname", surname);
        try {
            UpdateResult result = userCollection.updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error updating user by ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateByLogin(String login, String surname) {
        Bson filter = Filters.eq("login", login);
        Bson update = Updates.set("surname", surname);
        try {
            UpdateResult result = userCollection.updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error updating user by login: " + e.getMessage());
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

    @Override
    public List<User> findById(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<ObjectId> objectIds = ids.stream()
                .map(idMapper::stringToObjectId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (objectIds.isEmpty()) {
            return Collections.emptyList();
        }

        Bson filter = Filters.in("_id", objectIds);
        try {
            List<UserEntity> entities = userCollection.find(filter).into(new ArrayList<>());
            return mapper.toDataList(entities);
        } catch (MongoException e) {
            System.err.println("Error finding users by ID list: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
