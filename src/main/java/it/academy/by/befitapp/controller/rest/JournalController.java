package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.JournalSearchDto;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.service.api.IJournalService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/{id_profile}/journal/food")
public class JournalController {
    private final IJournalService iJournalService;

    public JournalController(IJournalService iJournalService) {
        this.iJournalService = iJournalService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@PathVariable("id_profile")Long idProfile,
                                 @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                 @RequestParam(value = "size",required = false,defaultValue = "30")Integer size){
        JournalSearchDto journalSearchDto = new JournalSearchDto();
        journalSearchDto.setPage(page);
        journalSearchDto.setSize(size);
        Page<Journal> dairies = this.iJournalService.getAll(idProfile, journalSearchDto);
        return new ResponseEntity<>(dairies, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byDay/{day}")
    public ResponseEntity<?> getAllByDay(@PathVariable("id_profile")Long idProfile,
                                 @PathVariable("day")Integer day){
        JournalSearchDto journalSearchDto = new JournalSearchDto();
        journalSearchDto.setDay(day);
        Page<Journal> journalPage = this.iJournalService.getAll(idProfile, journalSearchDto);
        List<Journal> journals = journalPage.getContent();
        //здесь надо соединить с методом которые считает калорийность за день и вывести через модель пользователю
        return new ResponseEntity<>(journals, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id_food}")
    public ResponseEntity<?> get(@PathVariable("id_profile")Long idProfile,
                                 @PathVariable("id_food") Long idFood){
        Journal journal = this.iJournalService.get(idProfile, idFood);
        return new ResponseEntity<>(journal,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id_profile")Long id,
                                  @RequestBody Journal dairy){
        if (dairy.getProduct()!=null && dairy.getDish()!=null){
            return new ResponseEntity<>("Вы не можете выбрать одновременно и продукт, и блюдо",HttpStatus.OK);
        }
        Long dairyId = this.iJournalService.save(dairy, id);
        return new ResponseEntity<>(dairyId,HttpStatus.CREATED);
    }


    //переписать апдейт и делет
    @RequestMapping(method = RequestMethod.PUT, value = "/{id_food}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id_profile")Long idProfile,
                                    @PathVariable("id_food")Long idFood,
                                    @PathVariable("dt_update")Long dtUpdate,
                                    @RequestBody Journal dairy){
        this.iJournalService.update(dairy,idProfile,idFood);//переписать
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_food}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id_profile") Long idProfile,
                                    @PathVariable("id_food")Long idFood,
                                    @PathVariable("dt_update")Long dtUpdate){
        this.iJournalService.delete(idFood);//переписать
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
