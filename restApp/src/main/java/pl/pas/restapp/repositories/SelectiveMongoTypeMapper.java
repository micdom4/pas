package pl.pas.restapp.repositories;

import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.TypeInformation;
import pl.pas.restapp.model.Order;

public class SelectiveMongoTypeMapper extends DefaultMongoTypeMapper {

    public SelectiveMongoTypeMapper(MongoMappingContext mappingContext) {
        super(DEFAULT_TYPE_KEY, mappingContext);
    }

    @Override
    public void writeType(TypeInformation<?> type, Bson document) {
        if (Order.class.isAssignableFrom(type.getType())) {

            super.writeType(type, document);
        }
    }

    @Override
    public void writeType(Class<?> type, Bson document) {
        if (Order.class.isAssignableFrom(type)) {

            super.writeType(type, document);
        }
    }
}
