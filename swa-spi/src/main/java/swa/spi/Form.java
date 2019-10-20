package swa.spi;

import java.util.List;
import java.util.Map;

public interface Form extends Html {

  List<FormElement> getElements(Map<String, String[]> queryParams, String[] splat) throws Exception;

  default String getSubmitButtonName() {
    return "Submit";
  }

  default String getSubmitButtonValue() {
    return "Submit";
  }

  default Link execute(Map<String, String[]> queryParams) throws Exception {
    return Link.root();
  }

  default boolean hasValue(String name, Map<String, String[]> queryParams) {
    return queryParams.get(name) != null;
  }

  default String getValue(String name, Map<String, String[]> queryParams, String errorMessage) {
    String[] values = queryParams.get(name);
    if (values == null || values.length == 0) {
      throw new RuntimeException(errorMessage);
    }
    return values[0];
  }
}
