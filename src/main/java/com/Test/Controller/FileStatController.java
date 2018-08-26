package com.Test.Controller;

import com.Test.Model.FileStat;
import com.Test.Service.FileStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileStatController {

    @Autowired
    FileStatService fileStatService;

    @RequestMapping(value = "/fileStat", method = RequestMethod.GET, produces = "application/json")
    public FileStat getFileStat(@RequestParam(value = "name")String name){
        return fileStatService.getByName(name);
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    public List<FileStat> getFileNameLike(){
        return fileStatService.getAll();
    }

}
