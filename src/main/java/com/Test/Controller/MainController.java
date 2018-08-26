package com.Test.Controller;

import com.Test.Service.FileStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class MainController {

    @Autowired
    FileStatService fileStatService;

    @GetMapping("/")
    public String main(){
        return "index";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file){

        if (saveFile(file) != null){
            fileStatService.saveFileStat(saveFile(file));
            return "redirect:/";
        }else {
            return "Error";
        }
    }


    private File saveFile(MultipartFile multipartFile){
//        String folderName = "upload_files/";
        if (!multipartFile.isEmpty()) {
            try {
                byte[] bytes = multipartFile.getBytes();
                File file = new File("upload_files/"+multipartFile.getOriginalFilename());
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(file));
                stream.write(bytes);
                stream.close();
                return file;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

}
