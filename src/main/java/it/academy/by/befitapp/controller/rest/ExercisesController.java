package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.service.api.IExercisesService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/profile/{id_profile}/journal/active")
public class ExercisesController {
    private final IExercisesService iExercisesService;

    public ExercisesController(IExercisesService iExercisesService) {
        this.iExercisesService = iExercisesService;
    }
//узнать как передавать дату в реквест параметр она передается но смотря в каком формате
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@PathVariable("id_profile")Long id,
                                 @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                 @RequestParam(value = "size", required = false,defaultValue = "30")Integer size,
                                 @RequestParam(value = "dt_start",required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                 @RequestParam(value = "dt_end",required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        ExercisesAndWeightSearchDto exercisesAndWeightSearchDto = new ExercisesAndWeightSearchDto();
        exercisesAndWeightSearchDto.setPage(page);
        exercisesAndWeightSearchDto.setSize(size);
        exercisesAndWeightSearchDto.setStart(start);
        exercisesAndWeightSearchDto.setEnd(end);
        Page<Exercise> exercisesPage = this.iExercisesService.getAll(id, exercisesAndWeightSearchDto);
        List<Exercise> exercises = exercisesPage.getContent();
        return new ResponseEntity<>(exercises, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,value = ("/{id_active}"))
    public ResponseEntity<?> get(@PathVariable("id_profile")Long idProfile,
                                 @PathVariable("id_active")Long idActive){
        Exercise exercise = this.iExercisesService.get(idProfile,idActive);
        return new ResponseEntity<>(exercise,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id_profile")Long id,
                                  @RequestBody Exercise exercise){
        Long  exerciseId= this.iExercisesService.save(exercise, id);
        return new ResponseEntity<>(exerciseId,HttpStatus.CREATED);
    }

    //переписать апдейт и делит
    @RequestMapping(method = RequestMethod.PUT, value = "/{id_active}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id_profile")Long idProfile,
                                    @PathVariable("id_active")Long idActive,
                                    @PathVariable("dt_update")Long dtUpdate,
                                    @RequestBody Exercise exercise){
        this.iExercisesService.update(exercise,idProfile,idActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_active}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id_profile")Long id,
                                    @PathVariable("id_active")Long idActive,
                                    @PathVariable("dt_update")Long dtUpdate){
        this.iExercisesService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
