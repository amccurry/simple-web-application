package swa.spi;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageSection {

  String sectionTitle;

  List<PageButton> pageButtons;

  List<PageElement> pageElements;

  List<ChartElement> chartElements;

}
