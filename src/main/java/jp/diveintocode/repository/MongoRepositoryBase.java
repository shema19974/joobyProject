package jp.diveintocode.repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
public class MongoRepositoryBase {

  Logger logger = LoggerFactory.getLogger(MongoRepositoryBase.class);

  protected final MongoClient mongoClient;
  protected final MongoDatabase database;
  protected final MongoCollection collection;

  public MongoRepositoryBase(String repoName, Class<?> documentClass) {
    String databaseName = config().getString("mongodb.databaseName");
    mongoClient = MongoClients.create();
    CodecRegistry codecRegistry = getRegistry();
    database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
    this.collection = database.getCollection(repoName, documentClass);
  }

  public MongoRepositoryBase(String repoName, Class<?> documentClass, Config config) {
    String databaseName = config.getString("mongodb.databaseName");
    mongoClient = MongoClients.create();
    CodecRegistry codecRegistry = getRegistry();
    database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
    this.collection = database.getCollection(repoName, documentClass);
  }

  private CodecRegistry getRegistry() {
    return CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
  }

  public void drop() {
    collection.drop();
  }

  @Nonnull
  public Config config() {
    return ConfigFactory.parseResources("application.conf");
  }
}
