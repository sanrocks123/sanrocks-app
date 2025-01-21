package com.sanrocks.utils;

import com.sanrocks.tax.AisToTradeBookReconciliationUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.SourceStringReader;

@Slf4j
public class PlantUmlUtils {

  public void getUMLBlocks(final String source) throws URISyntaxException, IOException {

    String content = Files.readString(Paths.get(getUri(source)));
    log.info("\n\n{}\n", content);

    SourceStringReader sourcePuml = new SourceStringReader(content);
    log.info("blocks, count: {}", sourcePuml.getBlocks().size());
    sourcePuml
        .getBlocks()
        .forEach(
            blockUml -> {
              log.info("startLine: {}\n", blockUml.getStartLine());
              blockUml
                  .getData()
                  .forEach(
                      cc -> {
                        log.info("{} {}", cc.getLocation(), cc.trin());
                        if (cc.toString2().endsWith("{")) {
                          // apply recursion for
                          log.info("inner block found, needs recursion");
                        }
                      });
            });
  }

  private URI getUri(final String filepath) throws URISyntaxException {
    return Objects.requireNonNull(AisToTradeBookReconciliationUtil.class.getResource(filepath))
        .toURI();
  }
}
