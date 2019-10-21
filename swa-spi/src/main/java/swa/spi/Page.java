package swa.spi;

import java.util.List;
import java.util.Map;

public interface Page extends Html {

  List<PageSection> getPagesSections(Map<String, String[]> queryParams, String[] splat) throws Exception;
  
}
