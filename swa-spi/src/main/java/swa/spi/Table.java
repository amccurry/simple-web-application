package swa.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Table extends Html {

  List<String> getHeaders(Map<String, String[]> queryParams) throws IOException;

  List<Row> getRows(Map<String, String[]> queryParams) throws IOException;

  default List<String> getActions(Map<String, String[]> queryParams) throws IOException {
    return new ArrayList<>();
  }

  default Link execute(String action, String[] ids) throws Exception {
    return Link.root();
  }

}
