package jp.diveintocode.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import com.typesafe.config.Config;
import jp.diveintocode.document.TestSuite;

import javax.inject.Inject;
import javax.inject.Singleton;
import jp.diveintocode.exception.ApiException;
import jp.diveintocode.exception.ApiExceptionConstant;

import static com.mongodb.client.model.Filters.*;

@Singleton
public class TestSuitesRepository extends MongoRepositoryBase {

  public TestSuitesRepository() {
    super("testSuites", TestSuite.class);
    initialize();
  }

  @Inject
  public TestSuitesRepository(Config conf) {
    super("testSuites", TestSuite.class, conf);
    initialize();
  }

  public void initialize() {
    IndexOptions indexOptions = new IndexOptions().unique(true);
    collection.createIndex(Indexes.ascending("code"), indexOptions);
  }

  public void insertOne(TestSuite testSuite) {
    collection.insertOne(testSuite);
  }

  /**
   * delete&insertで更新します。
   *
   * @param testSuite
   */
  public void updateOne(TestSuite testSuite) {
    deleteOne(testSuite.getCode());
    insertOne(testSuite);
  }

  public DeleteResult deleteOne(String code) {
    return collection.deleteOne(eq("code", code));
  }

  public TestSuite findOne(String code) {
    Object object = collection.find(eq("code", code)).first();
    if (object == null){
      throw new ApiException(ApiExceptionConstant.ILLEGAL_ARGUMENT_EXCEPTION, "Code is invalid.");
    }
    return (TestSuite) object;
  }
}
