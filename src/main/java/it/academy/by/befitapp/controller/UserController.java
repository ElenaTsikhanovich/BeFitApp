package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.service.api.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
        User user = this.iUserService.get(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        Page<User> users = this.iUserService.getAll(listDto);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody User user){
        Long userId = this.iUserService.save(user);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody User user){
        this.iUserService.update(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.iUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
