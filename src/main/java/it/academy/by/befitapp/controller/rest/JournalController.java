package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.journal.JournalOfDayDto;
import it.academy.by.befitapp.dto.journal.JournalSearchDto;
import it.academy.by.befitapp.dto.journal.JournalUpdateDto;
import it.academy.by.befitapp.dto.NutrientDto;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.service.api.IJournalService;
import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.utils.ConvertTime;
import it.academy.by.befitapp.service.colculator.ICalculator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/{id_profile}/journal/food")
public class JournalController {
    private final IJournalService iJournalService;
    private final ICalculator iCalculator;
    private final IProfileService iProfileService;

    public JournalController(IJournalService iJournalService, ICalculator iCalculator, IProfileService iProfileService) {
        this.iJournalService = iJournalService;
        this.iCalculator = iCalculator;
        this.iProfileService = iProfileService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@PathVariable("id_profile") Long idProfile,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "30") Integer size) {
        JournalSearchDto journalSearchDto = new JournalSearchDto();
        journalSearchDto.setPage(page);
        journalSearchDto.setSize(size);
        Page<Journal> dairiesPage = this.iJournalService.getAll(idProfile, journalSearchDto);
        List<Journal> journals = dairiesPage.getContent();
        return new ResponseEntity<>(journals, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/byDay/{day}")
    public ResponseEntity<?> getAllByDay(@PathVariable("id_profile") Long idProfile,
                                         @PathVariable("day") Long day,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "30") Integer size) {
        JournalSearchDto journalSearchDto = new JournalSearchDto();
        journalSearchDto.setDay(day);
        journalSearchDto.setPage(page);
        journalSearchDto.setSize(size);
        Page<Journal> journalPage = this.iJournalService.getAll(idProfile, journalSearchDto);
        List<Journal> journals = journalPage.getContent();
        JournalOfDayDto journalOfDayDto = new JournalOfDayDto();
        journalOfDayDto.setJournals(journals);
        journalOfDayDto.setNutrientDto(this.iCalculator.nutrientsInJournal(journals));
        Double caloriesNorm = this.iCalculator.getCaloriesNorm(this.iProfileService.get(idProfile));
        journalOfDayDto.setDayNorm(caloriesNorm);
        journalOfDayDto.setGoal(this.iCalculator.caloriesForGoal(this.iProfileService.get(idProfile)));
        return new ResponseEntity<>(journalOfDayDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id_food}")
    public ResponseEntity<?> get(@PathVariable("id_profile") Long idProfile,
                                 @PathVariable("id_food") Long idFood) {
        Journal journal = this.iJournalService.get(idProfile, idFood);
        JournalUpdateDto journalUpdateDto = new JournalUpdateDto();
        journalUpdateDto.setJournal(journal);
        journalUpdateDto.setUpdateTime(ConvertTime.fromDateToMilli(journal.getUpdateTime()));
        NutrientDto nutrientDto = this.iCalculator.nutrientsInJournal(List.of(journal));
        journalUpdateDto.setNutrientDto(nutrientDto);
        return new ResponseEntity<>(journalUpdateDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id_profile") Long id,
                                  @RequestBody Journal dairy) {
        if (dairy.getProduct() != null && dairy.getDish() != null) {
            return new ResponseEntity<>("Вы не можете выбрать одновременно и продукт, и блюдо", HttpStatus.OK);
        }
        Long dairyId = this.iJournalService.save(dairy, id);
        return new ResponseEntity<>(dairyId, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id_food}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id_profile") Long idProfile,
                                    @PathVariable("id_food") Long idFood,
                                    @PathVariable("dt_update") Long dtUpdate,
                                    @RequestBody Journal dairy) {
        if (dairy.getProduct() != null && dairy.getDish() != null) {
            return new ResponseEntity<>("Вы не можете выбрать одновременно и продукт, и блюдо", HttpStatus.OK);
        }
        this.iJournalService.update(dairy, idProfile, idFood,dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_food}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id_profile") Long idProfile,
                                    @PathVariable("id_food") Long idFood,
                                    @PathVariable("dt_update") Long dtUpdate) {
        this.iJournalService.delete(idProfile,idFood,dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
