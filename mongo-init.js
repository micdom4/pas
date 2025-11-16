

db.resources.drop();
db.Users.drop();
db.allocations.drop();
console.log("Existing collections dropped.");

console.log("Creating 'clients' collection...");

db.createCollection("clients", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            title: "Client Object Validation",
            required: ["_id", "name", "surname", "version"],
            properties: {
                _id: {
                    bsonType: ["int", "long"], 
                    description: "Must be a unique identifier"
                },
                name: {
                    bsonType: "string",
                    pattern: "^[A-Z][a-z]{1,30}$",
                    description: "'name' must be a string only of alphabetic characters between 1-30 length, and is required"
                },
                surname: {
                    bsonType: "string",
                    pattern: "^[A-Z][a-z]{1,30}$",
                    description: "'surname' must be a string only of alphabetic characters between 1-30 length, and is required"
                },
                version: {
                    bsonType: ["int", "long"], 
                    minimum: 0,
                    description: "'version' is a required long for optimistic locking"
                }
            },
            additionalProperties: false 
        }
    }
} );

console.log("Creating 'orders' collection...");
db.createCollection("orders", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            title: "Order Object Validation",
            required: [
                "_id",
                "client",
                "worker",
                "price",
                "startTime",
                "active",
                "destination",
                "version",
                "_class"
            ],
            properties: {
                _id: {
                    bsonType: ["int", "long"],
                    description: "Must be a unique order ID"
                },
                client: {
                    bsonType: "object",
                    description: "DBRef to the client who made the order",
                    required: ["$ref", "$id"],
                    properties: {
                        "$ref": {
                            bsonType: "string",
                            description: "The referenced collection name (e.g., 'clients')"
                        },
                        "$id": {
                            bsonType: ["int", "long"],
                            description: "The _id of the referenced client"
                        }
                    },
                    additionalProperties: false
                },
                worker: {
                    bsonType: "object",
                    description: "DBRef to the worker assigned to the order",
                    required: ["$ref", "$id"],
                    properties: {
                        "$ref": {
                            bsonType: "string",
                            description: "The referenced collection name (e.g., 'workers')"
                        },
                        "$id": {
                            bsonType: ["int", "long"],
                            description: "The _id of the referenced worker"
                        }
                    },
                    additionalProperties: false
                },
                price: {
                    bsonType: ["double", "int", "long"],
                    description: "Total price of the order, must be positive",
                    minimum: 0
                },
                startTime: {
                    bsonType: "date",
                    description: "When the order was started"
                },
                endTime: {
                    bsonType: ["date", "null"],
                    description: "When the order was completed (null if active)"
                },
                active: {
                    bsonType: "bool",
                    description: "Whether the order is currently in progress"
                },
                destination: {
                    bsonType: "string",
                    description: "The final destination address"
                },
                version: {
                    bsonType: ["int", "long"],
                    description: "Document version for optimistic locking",
                    minimum: 0
                },
                _class: {
                    bsonType: "string",
                    description: "The subclass of the order",
                    enum: ["FoodOrder", "TaxiOrder"]
                },
                foodDetails: {
                    bsonType: "object",
                    description: "Details specific to a food order",
                    required: ["restaurantId", "restaurantAddress"],
                    properties: {
                        restaurantId: { bsonType: ["int", "long"] },
                        restaurantAddress: { bsonType: "string" }
                    },
                    additionalProperties: false
                },
                taxiDetails: {
                    bsonType: "object",
                    description: "Details specific to a taxi order",
                    required: ["type", "startLocation", "licensePlate"],
                    properties: {
                        type: {
                            bsonType: "string",
                            enum: ["NORMAL", "COMFORT", "PREMIUM", "WOMAN"]
                        },
                        startLocation: { bsonType: "string" },
                        licensePlate: { bsonType: "string" }
                    },
                    additionalProperties: false
                }
            },
            oneOf: [
                {
                    properties: {
                        _class: { enum: ["FoodOrder"] }
                    },
                    required: ["foodDetails"]
                },
                {
                    properties: {
                        _class: { enum: ["TaxiOrder"] }
                    },
                    required: ["taxiDetails"]
                }
            ],
            additionalProperties: false
        }
    }
});


console.log("Attempting to insert documents into 'clients'...");
db.clients.insertMany([
    { _id: 1, name: 'John', surname: 'Doe', version: 0 },
    { _id: 2, name: 'Anna', surname: 'Smith', version: 0 },
    { _id: 3, name: 'Robert', surname: 'Johnson', version: 0 },
    { _id: 4, name: 'Emily', surname: 'Davis', version: 0 },
    { _id: 5, name: 'Michael', surname: 'Brown', version: 0 }
]);
console.log("Successfully inserted " + db.clients.countDocuments() + " documents into 'clients'.");

console.log("Attempting to insert documents into 'orders' one by one...");

const orders = [
    {
        _id: 1001,
        client: { "$ref": "clients", "$id": 1 },
        worker: { "$ref": "workers", "$id": 201 },
        price: 49.99,
        startTime: new ISODate("2025-10-09T12:00:00Z"),
        endTime: null,
        active: true,
        destination: 'Central Park',
        version: 0,
        _class: 'FoodOrder',
        foodDetails: {
            restaurantId: 501,
            restaurantAddress: '123 Main St'
        }
    },
    {
        _id: 1004,
        client: { "$ref": "clients", "$id": 2 },
        worker: { "$ref": "workers", "$id": 202 },
        price: 29.99,
        startTime: new ISODate("2025-10-09T10:00:00Z"),
        endTime: null,
        active: true,
        destination: 'Times Square',
        version: 0,
        _class: 'FoodOrder',
        foodDetails: {
            restaurantId: 502,
            restaurantAddress: '456 Elm St'
        }
    },
    {
        _id: 1005,
        client: { "$ref": "clients", "$id": 3 },
        worker: { "$ref": "workers", "$id": 203 },
        price: 34.50,
        startTime: new ISODate("2025-10-09T18:15:00Z"),
        endTime: new ISODate("2025-10-09T18:45:00Z"),
        active: false,
        destination: 'Brooklyn Bridge',
        version: 0,
        _class: 'FoodOrder',
        foodDetails: {
            restaurantId: 503,
            restaurantAddress: '789 Maple Ave'
        }
    },
    {
        _id: 1003,
        client: { "$ref": "clients", "$id": 1 },
        worker: { "$ref": "workers", "$id": 401 },
        price: 79.00,
        startTime: new ISODate("2025-10-09T14:00:00Z"),
        endTime: new ISODate("2025-10-09T14:30:00Z"),
        active: false,
        destination: 'Downtown Plaza',
        version: 0,
        _class: 'TaxiOrder',
        taxiDetails: {
            type: 'PREMIUM',
            startLocation: 'North Square',
            licensePlate: 'ABC-1234'
        }
    },
    {
        _id: 1008,
        client: { "$ref": "clients", "$id": 5 },
        worker: { "$ref": "workers", "$id": 402 },
        price: 59.00,
        startTime: new ISODate("2025-10-09T11:30:00Z"),
        endTime: null,
        active: true,
        destination: 'Union Station',
        version: 0,
        _class: 'TaxiOrder',
        taxiDetails: {
            type: 'COMFORT',
            startLocation: 'East Village',
            licensePlate: 'XYZ-9876'
        }
    },
    {
        _id: 1009,
        client: { "$ref": "clients", "$id": 3 },
        worker: { "$ref": "workers", "$id": 403 },
        price: 99.00,
        startTime: new ISODate("2025-10-09T19:00:00Z"),
        endTime: null,
        active: true,
        destination: 'Airport Terminal B',
        version: 0,
        _class: 'TaxiOrder',
        taxiDetails: {
            type: 'PREMIUM',
            startLocation: 'City Hall',
            licensePlate: 'JKL-5678'
        }
    },
    {
        _id: 1010,
        client: { "$ref": "clients", "$id": 4 },
        worker: { "$ref": "workers", "$id": 404 },
        price: 45.00,
        startTime: new ISODate("2025-10-09T08:30:00Z"),
        endTime: new ISODate("2025-10-09T09:00:00Z"),
        active: false,
        destination: 'SoHo',
        version: 0,
        _class: 'TaxiOrder',
        taxiDetails: {
            type: 'NORMAL',
            startLocation: 'Chelsea Market',
            licensePlate: 'MNO-2222'
        }
    }
];

orders.forEach(order => {
    try {
        db.orders.insertOne(order);
        console.log("Successfully inserted order _id: " + order._id);
    } catch (e) {
        console.error("Failed to insert order _id: " + order._id, e);
    }
});

console.log("Finished inserting orders. Total documents in 'orders': " + db.orders.countDocuments());
console.log("\nDatabase initialization complete! : )))");
