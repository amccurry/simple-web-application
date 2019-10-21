package swa.spi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageButton implements PageAction {

  String name;

  String link;

  @Builder.Default
  PageActionType type = PageActionType.BUTTON;

}
