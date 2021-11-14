package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;

@RestController
@RequestMapping("/query")
public class QueryController extends BaseController {
    @GetMapping("/{query}")
    public Object query(@PathVariable String query) {
        return queryManager.getAllData(query);
    }
}
