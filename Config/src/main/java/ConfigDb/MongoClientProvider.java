package ConfigDb;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
public enum MongoClientProvider {
    INSTANCE;

    private MongoClient client;
    private String name = "onlineElectronicStore";
    private String uri =  "mongodb://localhost:27017";

    public synchronized void init() {
        if (client == null) {
            client = MongoClients.create(MongoConfig.buildSettings(uri));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try{
                    client.close();
                }catch(Exception e){

                }
            }));
        }
    }

    public MongoClient client(){
        if(client == null)throw new IllegalStateException("MongoClientProvider no a sido inicialido desde init");
        return client;
    }

    public MongoDatabase database(){
        return client.getDatabase(name);
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz){
        if(client == null)
            throw new IllegalStateException("MongoClientProvider no a sido inicialido desde init");

        MongoDatabase db =client.getDatabase(this.name);
        return db.getCollection(collectionName, clazz);
    }
}
