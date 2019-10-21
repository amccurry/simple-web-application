package swa.spi;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChartElement {

  @Builder.Default
  String id = UUID.randomUUID()
                  .toString();

  List<String> chartLabels;

  List<ChartDataset> datasets;

}
