package swa.spi;

public class Link {

  private static final Link ROOT = new Link("");

  public static Link root() {
    return ROOT;
  }

  private final String _href;

  public Link(String... parts) {
    if (parts == null) {
      _href = "";
    } else {
      StringBuilder builder = new StringBuilder();
      for (String part : parts) {
        builder.append(part);
      }
      _href = builder.toString();
    }
  }

  @Override
  public String toString() {
    return _href;
  }

  public static Link create(String... parts) {
    return new Link(parts);
  }

}
