package swa.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import spark.Service;
import swa.SWABuilder;
import swa.spi.ChartDataset;
import swa.spi.ChartElement;
import swa.spi.CheckBoxFormElement;
import swa.spi.Column;
import swa.spi.Form;
import swa.spi.FormElement;
import swa.spi.Link;
import swa.spi.Option;
import swa.spi.Page;
import swa.spi.PageButton;
import swa.spi.PageElement;
import swa.spi.PageSection;
import swa.spi.ReadOnlyFormElement;
import swa.spi.Row;
import swa.spi.SelectFormElement;
import swa.spi.Table;
import swa.spi.TextFormElement;

public class Example {

  public static void main(String[] args) throws IOException {
    Service service = Service.ignite();

    Table table1 = new Table() {

      @Override
      public String getLinkName() {
        return "test1";
      }

      @Override
      public List<Row> getRows(Map<String, String[]> queryParams) {
        return Arrays.asList(createRow("1"), createRow("2"), createRow("3"));
      }

      private Row createRow(String id) {
        return Row.builder()
                  .id(id)
                  .columns(Arrays.asList(Column.builder()
                                               .value("test1")
                                               .build(),
                      Column.builder()
                            .value("test2")
                            .build()))
                  .build();
      }

      @Override
      public List<String> getActions(Map<String, String[]> queryParams) {
        return Arrays.asList("action1", "action2");
      }

      @Override
      public List<String> getHeaders(Map<String, String[]> queryParams) {
        return Arrays.asList("col1", "col2");
      }

      @Override
      public Link execute(String action, String[] ids) throws Exception {
        System.out.println(action + " " + Arrays.asList(ids));
        return Link.root();
      }

    };

    Table table2 = new Table() {

      @Override
      public String getLinkName() {
        return "test2";
      }

      @Override
      public List<Row> getRows(Map<String, String[]> queryParams) {
        return Arrays.asList(createRow("1"), createRow("2"), createRow("3"));
      }

      private Row createRow(String id) {
        return Row.builder()
                  .id(id)
                  .columns(Arrays.asList(Column.builder()
                                               .value("test1")
                                               .build(),
                      Column.builder()
                            .value("test2")
                            .link(Link.create("/page2/", id))
                            .build()))
                  .build();
      }

      @Override
      public List<String> getHeaders(Map<String, String[]> queryParams) {
        return Arrays.asList("col1 another line", "col2");
      }
    };

    Form form = new Form() {

      @Override
      public String getLinkName() throws IOException {
        return "create";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams, String[] splat) {
        FormElement element1 = TextFormElement.builder()
                                              .name("name")
                                              .label("label")
                                              .defaultValue("default")
                                              .build();

        List<Option> options = Arrays.asList(Option.builder()
                                                   .label("olabel")
                                                   .value("ovalue")
                                                   .build());
        SelectFormElement element2 = SelectFormElement.builder()
                                                      .label("label")
                                                      .name("name")
                                                      .options(options)
                                                      .build();

        CheckBoxFormElement element3 = CheckBoxFormElement.builder()
                                                          .value("text")
                                                          .label("l3")
                                                          .name("l3")
                                                          .build();

        CheckBoxFormElement element4 = CheckBoxFormElement.builder()
                                                          .value("text")
                                                          .label("l3")
                                                          .name("l3")
                                                          .defaultValue(true)
                                                          .build();

        ReadOnlyFormElement element5 = ReadOnlyFormElement.builder()
                                                          .value("nice")
                                                          .label("l23432")
                                                          .name("name1231")
                                                          .build();

        return Arrays.asList(element1, element2, element3, element4, element5);
      }

    };

    Form formError1 = new Form() {

      @Override
      public String getLinkName() throws IOException {
        return "error1";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams, String[] splat) {
        FormElement element1 = TextFormElement.builder()
                                              .name("name")
                                              .label("label")
                                              .defaultValue("default")
                                              .build();

        return Arrays.asList(element1);
      }

      @Override
      public Link execute(Map<String, String[]> queryParams) throws Exception {
        throw new RuntimeException("this is the error");
      }

    };

    Form formError2 = new Form() {

      @Override
      public String getLinkName() throws IOException {
        return "error2";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams, String[] splat) {
        throw new RuntimeException("this is the error");
      }

    };

    Page page1 = new Page() {

      @Override
      public String getLinkName() throws IOException {
        return "page1";
      }

      @Override
      public List<PageSection> getPagesSections(Map<String, String[]> queryParams, String[] splat) throws Exception {
        ChartDataset dataset1 = ChartDataset.builder()
                                            .label("abc")
                                            .values(Arrays.asList(1, 2, 3, 4))
                                            .build();
        ChartDataset dataset2 = ChartDataset.builder()
                                            .label("def")
                                            .values(Arrays.asList(4, 3, 2, 1))
                                            .build();
        ChartDataset dataset3 = ChartDataset.builder()
                                            .label("zxd")
                                            .values(Arrays.asList(1, 4, 3, 2))
                                            .build();

        ChartElement chart1 = ChartElement.builder()
                                          .datasets(Arrays.asList(dataset1, dataset2, dataset3))
                                          .chartLabels(Arrays.asList("a", "b", "c", "d"))
                                          .build();

        ChartDataset dataset4 = ChartDataset.builder()
                                            .label("zxd")
                                            .values(Arrays.asList(1, 4, 3, 2))
                                            .build();

        ChartElement chart2 = ChartElement.builder()
                                          .datasets(Arrays.asList(dataset4))
                                          .chartLabels(Arrays.asList("e", "f", "g", "h"))
                                          .build();

        ChartDataset dataset5 = ChartDataset.builder()
                                            .label("zxd")
                                            .values(Arrays.asList(1, 4, 3, 2))
                                            .build();

        ChartElement chart3 = ChartElement.builder()
                                          .datasets(Arrays.asList(dataset5))
                                          .chartLabels(Arrays.asList("e", "f", "g", "h"))
                                          .build();

        return Arrays.asList(PageSection.builder()
                                        .sectionTitle("section title1")
                                        .chartElements(Arrays.asList(chart1, chart2, chart3))
                                        .pageElements(Arrays.asList(PageElement.builder()
                                                                               .label("label1")
                                                                               .value("test")
                                                                               .build()))
                                        .build());
      }

    };

    Page page2 = new Page() {

      @Override
      public String getLinkName() throws IOException {
        return "page2";
      }

      @Override
      public List<PageSection> getPagesSections(Map<String, String[]> queryParams, String[] splat) throws Exception {
        if (splat == null || splat.length == 0) {
          throw new RuntimeException("Missing id");
        }
        String id = splat[0];
        List<PageElement> pageElements = Arrays.asList(PageElement.builder()
                                                                  .label("label1")
                                                                  .value(id)
                                                                  .build());
        return Arrays.asList(PageSection.builder()
                                        .pageElements(pageElements)
                                        .build());
      }
    };

    Page page3 = new Page() {

      @Override
      public String getLinkName() throws IOException {
        return "page3";
      }

      @Override
      public List<PageSection> getPagesSections(Map<String, String[]> queryParams, String[] splat) throws Exception {
        List<PageElement> pageElements = Arrays.asList(PageElement.builder()
                                                                  .label("label1")
                                                                  .value("test")
                                                                  .build());
        PageButton button1 = PageButton.builder()
                                       .action("/")
                                       .name("Home")
                                       .build();
        PageButton button2 = PageButton.builder()
                                       .action("/")
                                       .name("Root")
                                       .build();
        List<PageButton> pageButtons = Arrays.asList(button1, button2);
        return Arrays.asList(PageSection.builder()
                                        .pageButtons(pageButtons)
                                        .pageElements(pageElements)
                                        .build());
      }
    };

    SWABuilder builder = SWABuilder.create(service);
    builder.startMenu()
           .addHtml(table1)
           .addHtml(table2)
           .addHtml(form)
           .addHtml(page1)
           .addHtml(formError1)
           .addHtml(formError2)
           .stopMenu()
           .startMenu("menu2")
           .addHtml(page3)
           .stopMenu()
           .addHtml(page2)
           .setApplicationName("Test App")
           .build("test1");

  }
}
