package io.prediction.examples.java.recommendations.tutorial2;

import io.prediction.examples.java.recommendations.tutorial1.TrainingData;
import io.prediction.examples.java.recommendations.tutorial1.Query;
import io.prediction.examples.java.recommendations.tutorial1.DataSource;
import io.prediction.examples.java.recommendations.tutorial1.DataSourceParams;

import io.prediction.controller.EmptyParams;
import io.prediction.controller.IEngineFactory;
import io.prediction.controller.java.JavaSimpleEngine;
import io.prediction.controller.java.JavaSimpleEngineBuilder;
import io.prediction.controller.java.JavaEngineParams;
import io.prediction.controller.java.JavaEngineParamsBuilder;
import io.prediction.workflow.JavaAPIDebugWorkflow;
import java.util.HashMap;

public class Runner1 {

  // During development, one can build a semi-engine, only add the first few layers. In this
  // particular example, we only add until dataSource layer
  private static class HalfBakedEngineFactory implements IEngineFactory {
    public JavaSimpleEngine<TrainingData, Object, Query, Float, Object> apply() {
      return new JavaSimpleEngineBuilder<
        TrainingData, Object, Query, Float, Object> ()
        .dataSourceClass(DataSource.class)
        .build();
    }
  }

  public static void runComponents(String filePath) {
    JavaEngineParams engineParams = new JavaEngineParamsBuilder()
      .dataSourceParams(new DataSourceParams(filePath))
      .build();

    JavaAPIDebugWorkflow.runEngine(
      "MyEngine",
      new HashMap<String, String>(),
      3, // verbose
      (new HalfBakedEngineFactory()).apply(),
      engineParams,
      null,
      new EmptyParams()
    );
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Error: Please specify the file path as argument");
      System.exit(1);
    }
    runComponents(args[0]);
    System.exit(0); // clean shutdown is needed for spark
  }
}