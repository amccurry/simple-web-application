package swa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Service;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import swa.spi.Form;
import swa.spi.Html;
import swa.spi.Menu;
import swa.spi.Page;
import swa.spi.Table;

public class SWABuilder {

  private static final String APPLICATION_NAME = "applicationName";
  private static final String PAGE_HTML = "page.html";
  private static final String ERROR_MESSAGE = "errorMessage";
  private static final String ERROR_HTML = "error.html";
  private static final String TABLE_HTML = "table.html";
  private static final String FORM_HTML = "form.html";
  private static final String SUBMIT_NAME = "submitName";
  private static final String SUBMIT_VALUE = "submitValue";
  private static final String ELEMENTS = "elements";
  private static final String HEADERS = "headers";
  private static final String ROWS = "rows";
  private static final String ACTIONS = "actions";
  private static final String LINK = "link";
  private static final String NAME = "name";
  private static final String DEFAULT_MENUS = "defaultMenus";
  private static final String CURRENT_MENU_NAME = "currentMenuName";
  private static final String WINDOW_TITLE = "windowTitle";
  private static final String PUBLIC = "/public";
  private static final String ID = "id";
  private static final String ACTION = "action";
  private static final String NAMED_MENUS = "namedMenus";

  private final Service _service;
  private final FreeMarkerEngine _engine = new FreeMarkerEngine();
  private final List<Menu> _defaultMenus = new ArrayList<>();
  private final Map<String, List<Menu>> _menus = new ConcurrentHashMap<>();
  private final Map<String, Html> _htmlElements = new ConcurrentHashMap<>();
  private final AtomicReference<List<Menu>> _currentMenuBuilder = new AtomicReference<>();
  private final AtomicReference<Logger> _logger = new AtomicReference<>();
  private final AtomicReference<String> _applicationName = new AtomicReference<>();

  private SWABuilder(Service service) {
    _service = service;
    _service.staticFileLocation(PUBLIC);
  }

  public SWABuilder startMenu() {
    _currentMenuBuilder.set(_defaultMenus);
    return this;
  }

  public SWABuilder startMenu(String name) {
    List<Menu> menus = _menus.get(name);
    if (menus == null) {
      _menus.put(name, menus = new ArrayList<>());
    }
    _currentMenuBuilder.set(menus);
    return this;
  }

  public SWABuilder stopMenu() {
    _currentMenuBuilder.set(null);
    return this;
  }

  public SWABuilder addHtml(Html html) throws IOException {
    List<Menu> menus = _currentMenuBuilder.get();
    if (menus != null) {
      menus.add(Menu.create(html));
    }
    if (html instanceof Page) {
      addPage((Page) html);
    } else if (html instanceof Form) {
      addForm((Form) html);
    } else if (html instanceof Table) {
      addTable((Table) html);
    }
    return this;
  }

  public SWABuilder setLogger(Logger logger) {
    _logger.set(logger);
    return this;
  }

  public SWABuilder setApplicationName(String applicationName) {
    _applicationName.set(applicationName);
    return this;
  }

  public void build(String defaultHtmlElement) {
    Logger logger = _logger.get();
    if (logger != null) {
      _service.before(
          (request, response) -> logger.info("request method {} uri {}", request.requestMethod(), request.raw()
                                                                                                         .getRequestURI()));
    }
    _service.get("/", (request, response) -> {
      Html html = _htmlElements.get(defaultHtmlElement);
      response.redirect(html.getLinkName());
      return null;
    });
  }

  private void addPage(Page page) throws IOException {
    String name = page.getLinkName();
    if (_htmlElements.containsKey(name)) {
      throw new RuntimeException("Alread contains page " + name);
    }
    _htmlElements.put(name, page);
    _service.get("/" + name, getPage(page), _engine);
    _service.get("/" + name + "/*", getPage(page), _engine);
  }

  private void addTable(Table table) throws IOException {
    String name = table.getLinkName();
    if (_htmlElements.containsKey(name)) {
      throw new RuntimeException("Already contains table " + name);
    }
    _htmlElements.put(name, table);
    _service.get("/" + name, getTable(table), _engine);
    _service.post("/" + name, postTable(table), _engine);
  }

  private void addForm(Form form) throws IOException {
    String name = form.getLinkName();
    if (_htmlElements.containsKey(name)) {
      throw new RuntimeException("Alread contains form " + name);
    }
    _htmlElements.put(name, form);
    _service.get("/" + name, getForm(form), _engine);
    _service.get("/" + name + "/*", getForm(form), _engine);
    _service.post("/" + name, postForm(form), _engine);
  }

  private TemplateViewRoute postForm(Form form) {
    return (request, response) -> {
      try {
        QueryParamsMap map = request.queryMap();
        response.redirect(form.execute(map.toMap())
                              .toString());
        return null;
      } catch (Exception e) {
        Map<String, Object> model = createModel(form);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute getForm(Form form) {
    return (request, response) -> {
      try {
        Map<String, String[]> map = request.queryMap()
                                           .toMap();
        Map<String, Object> model = createModel(form);
        model.put(SUBMIT_NAME, form.getSubmitButtonName());
        model.put(SUBMIT_VALUE, form.getSubmitButtonValue());
        model.put(ELEMENTS, form.getElements(map, request.splat()));
        return new ModelAndView(model, FORM_HTML);
      } catch (Exception e) {
        Map<String, Object> model = createModel(form);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute getPage(Page page) {
    return (request, response) -> {
      try {
        Map<String, String[]> map = request.queryMap()
                                           .toMap();
        Map<String, Object> model = createModel(page);
        model.put(ELEMENTS, page.getElements(map, request.splat()));
        return new ModelAndView(model, PAGE_HTML);
      } catch (Exception e) {
        Map<String, Object> model = createModel(page);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute postTable(Table table) {
    return (request, response) -> {
      try {
        String action = request.queryParams(ACTION);
        response.redirect(table.execute(action, request.queryParamsValues(ID))
                               .toString());
        return null;
      } catch (Exception e) {
        Map<String, Object> model = createModel(table);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute getTable(Table table) {
    return (request, response) -> {
      try {
        Map<String, String[]> map = request.queryMap()
                                           .toMap();
        Map<String, Object> model = createModel(table);
        model.put(HEADERS, table.getHeaders(map));
        model.put(ROWS, table.getRows(map));
        model.put(ACTIONS, table.getActions(map));
        return new ModelAndView(model, TABLE_HTML);
      } catch (Exception e) {
        Map<String, Object> model = createModel(table);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private Map<String, Object> createModel(Html html) throws IOException {
    Map<String, Object> model = new HashMap<>();
    model.put(WINDOW_TITLE, html.getWindowTitle());
    model.put(APPLICATION_NAME, _applicationName.get());
    model.put(CURRENT_MENU_NAME, html.getName());
    model.put(DEFAULT_MENUS, _defaultMenus);
    model.put(NAMED_MENUS, _menus);
    model.put(NAME, html.getName());
    model.put(LINK, html.getLinkName());
    return model;
  }

  public static SWABuilder create(Service service) {
    return new SWABuilder(service);
  }

}
