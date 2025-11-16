db.virtualMachines.drop();
db.users.drop();
db.vmAllocations.drop();

db.createCollection("virtualMachines");
db.createCollection("users");
db.createCollection("vmAllocations");

console.log("Existing collections dropped.");

console.log("Creating 'clients' collection...");

