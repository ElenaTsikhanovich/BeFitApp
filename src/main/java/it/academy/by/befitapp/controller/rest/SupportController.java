package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.utils.ProductBaseExelParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final ProductBaseExelParser productBaseExelParser;

    public SupportController(ProductBaseExelParser productBaseExelParser) {
        this.productBaseExelParser = productBaseExelParser;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResponseEntity<?> getFile(@RequestParam("name")String name,
                                     @RequestParam("file")MultipartFile file){
      if (!file.isEmpty()) {
          try {
              byte[]bytes = file.getBytes();
              BufferedOutputStream stream =
                      new BufferedOutputStream(new FileOutputStream(new File(name)));
              stream.write(bytes);
              stream.close();
              this.productBaseExelParser.parseXlsFileToDB(name);
              return new ResponseEntity<>("is ok", HttpStatus.OK);
          }catch (IOException e){
              e.printStackTrace();
          }
              return new ResponseEntity<>("No", HttpStatus.BAD_REQUEST);
          }
      else {
          return new ResponseEntity<>("Empty", HttpStatus.OK);

      }
    }




/*
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> fillingDB(@RequestParam("fileName")String fileName){
        this.productBaseExelParser.parseXlsFileToDB(fileName);
        return new ResponseEntity<>("Данные успешно загружены", HttpStatus.CREATED);
    }

 */
}

