package com.sanrocks.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class PlantUmlUtilsTest {

  @Test
  public void getUMLBlocks() throws URISyntaxException, IOException {
    String source = "/com/sanrocks/utils/plantuml/c4-example.puml";
    new PlantUmlUtils().getUMLBlocks(source);
  }
}
