package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final IProfileService iProfileService;

    public ProfileController(IProfileService iProfileService) {
        this.iProfileService = iProfileService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id_profile}")
    public ResponseEntity<?> get(@PathVariable("id_profile") Long id){
        Profile profile = this.iProfileService.get(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam (value = "page",required = false, defaultValue = "0")Integer page,
                                    @RequestParam(value = "size",required = false, defaultValue = "30")Integer size){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        Page<Profile> profiles = this.iProfileService.getAll(listDto);
        return new ResponseEntity<>(profiles,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Profile profile){
        Long profileId = this.iProfileService.save(profile);
        return new ResponseEntity<>(profileId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id_profile}")
    public ResponseEntity<?> update(@PathVariable("id_profile") Long id,
                                    @RequestBody Profile profile){
        this.iProfileService.update(profile,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_profile}")
    public ResponseEntity<?> delete(@PathVariable("id_profile") Long id){
        this.iProfileService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
