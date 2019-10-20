package swa.spi;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Menu {

  String name;

  String link;

  @Builder.Default
  String icon = "minus";

  public static Menu create(Html html) throws IOException {
    return Menu.builder()
               .name(html.getName())
               .link("/" + html.getLinkName())
               .icon(html.getIcon())
               .build();
  }

}
