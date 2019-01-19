package gr.uoa.di.dbm.restapi.controller;

import gr.uoa.di.dbm.restapi.service.ParserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parser")
public class ParserController {
    private final ParserServiceImpl parserServiceImpl;

    @Autowired
    public ParserController(ParserServiceImpl parserServiceImpl) {
        this.parserServiceImpl = parserServiceImpl;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void parseData(){
        parserServiceImpl.parseData();
    }
}
