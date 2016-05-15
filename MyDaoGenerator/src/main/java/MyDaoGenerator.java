import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "com.advocate.database");

        addTables(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void addTables(Schema schema) {
        Entity user = addUser(schema);
        Entity client = addClient(schema);
        Entity firm = addFirm(schema);



    }

    private static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("UserTable");
        user.addStringProperty("mobileNumber");
        user.addStringProperty("userIdLawyer").primaryKey();
        user.addStringProperty("emailId");
        user.addStringProperty("lawyerId").unique().notNull();
        user.addStringProperty("firstName");
        user.addStringProperty("lastName");

        user.addStringProperty("category");
        user.addStringProperty("firmNames");

        return user;

    }

    private static Entity addClient(Schema schema) {
        Entity client = schema.addEntity("ClientTable");
        client.addStringProperty("userIdClient").primaryKey();
        client.addStringProperty("clientFirstName");
        client.addStringProperty("clientLastName");
        client.addStringProperty("caseNumber").unique().notNull();
        client.addStringProperty("courtNumber");
        client.addStringProperty("ClientMobile");
        client.addStringProperty("briefDetails");
        client.addStringProperty("lawyerId");
        client.addDateProperty("nextDate");

        return client;

    }

    private static Entity addFirm(Schema schema) {
        Entity firm = schema.addEntity("FirmTable");
        firm.addStringProperty("firmName").unique().notNull();
        firm.addStringProperty("userIdFirm").primaryKey();
        firm.addStringProperty("lawyerIds");

        return firm;

    }

}
