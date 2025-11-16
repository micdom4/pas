package team.four.pas.repositories.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper
public interface VMAllocationMapper {
    default String objectIdToString(ObjectId objectId) {
        if (objectId == null) {
            return null;
        }
        return objectId.toHexString();
    }

    default ObjectId stringToObjectId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return new ObjectId(id);
    }
}
