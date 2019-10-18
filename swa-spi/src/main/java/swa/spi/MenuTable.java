package swa.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MenuTable {

  default List<Menu> getMenus() throws IOException {
    return new ArrayList<>();
  }

}
