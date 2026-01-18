try {
    db.users.drop();
    db.virtualMachines.drop();
    db.vmAllocations.drop();
} catch (e) {
    console.log("Failed :( rep -1");
}
console.log("Existing collections dropped.");

const userSchema = {
    $jsonSchema: {
        bsonType: "object",
        required: ["login", "password", "name", "surname", "type", "active"],
        properties: {
            _id: { bsonType: "objectId" },
            login: {
                bsonType: "string",
                description: "must be a string and is required (unique)"
            },
            password: {
                bsonType: "string",
                description: "must be a string and is required (unique)"
            },
            name: {
                bsonType: "string",
                description: "must be a string and is required"
            },
            surname: {
                bsonType: "string",
                description: "must be a string and is required"
            },
            type: {
                bsonType: "string",
                enum: ["CLIENT", "MANAGER", "ADMIN"],
                description: "must be one of 'CLIENT', 'MANAGER', or 'ADMIN' and is required"
            },
            active: {
                bsonType: "bool",
                description: "must be a boolean and is required"
            }
        }
    }
};

const vmSchema = {
    $jsonSchema: {
        bsonType: "object",
        required: ["cpuNumber", "ramGiB", "storageGiB"],
        properties: {
            _id: { bsonType: "objectId" },
            cpuNumber: {
                bsonType: "int",
                minimum: 1,
                description: "must be an integer (min 1) and is required"
            },
            ramGiB: {
                bsonType: "int",
                minimum: 1,
                description: "must be an integer (min 1) and is required"
            },
            storageGiB: {
                bsonType: "int",
                minimum: 1,
                description: "must be an integer (min 1) and is required"
            }
        }
    }
};

const vmAllocationSchema = {
    $jsonSchema: {
        bsonType: "object",
        required: ["client", "vm", "startTime"],
        properties: {
            _id: { bsonType: "objectId" },
            client: {
                bsonType: "object",
                description: "Embedded UserEntity document",
                required: ["_id", "login", "name", "surname", "type", "active"],
                properties: {
                    _id: { bsonType: "objectId" },
                    login: { bsonType: "string" },
                    name: { bsonType: "string" },
                    surname: { bsonType: "string" },
                    type: { bsonType: "string", enum: ["CLIENT", "MANAGER", "ADMIN"] },
                    active: { bsonType: "bool" }
                }
            },
            vm: {
                bsonType: "object",
                description: "Embedded VirtualMachineEntity document",
                required: ["_id", "cpuNumber", "ramGiB", "storageGiB"],
                properties: {
                    _id: { bsonType: "objectId" },
                    cpuNumber: { bsonType: "int", minimum: 1 },
                    ramGiB: { bsonType: "int", minimum: 1 },
                    storageGiB: { bsonType: "int", minimum: 1 }
                }
            },
            startTime: {
                bsonType: "date",
                description: "must be a date and is required"
            },
            endTime: {
                bsonType: ["date", "null"],
                description: "must be a date or null"
            }
        }
    }
};

try {
    db.createCollection("users", {
        validator: userSchema
    });
    db.createCollection("virtualMachines", {
        validator: vmSchema
    });
    db.createCollection("vmAllocations", {
        validator: vmAllocationSchema
    });
    db.createCollection("tokens", {
    });
} catch (e) {
    console.log("Failed :( rep -1");
}
console.log("Creating 'clients' collection...");

try {
    db.users.createIndex({ login: 1 }, { unique: true });
} catch (e) {
    console.log("Already Indexed!");
}