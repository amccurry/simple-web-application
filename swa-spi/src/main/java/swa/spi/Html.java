package swa.spi;

import java.io.IOException;

public interface Html {
  default String getName() throws IOException {
    return getLink();
  }

  String getLink() throws IOException;

  default String getWindowTitle() throws IOException {
    return getName();
  }

  default String getWindowName() throws IOException {
    return getName();
  }

  default String getIcon() {
    return "minus";
  }
}
