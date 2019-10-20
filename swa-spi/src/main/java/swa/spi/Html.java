package swa.spi;

import java.io.IOException;

public interface Html {
  default String getName() throws IOException {
    return getLinkName();
  }

  String getLinkName() throws IOException;

  default String getWindowTitle() throws IOException {
    return getName();
  }

  default String getIcon() {
    return "minus";
  }
}
