package it.academy.by.befitapp.controller.rest;

import it.academy.by.befitapp.dto.exercises.ExerciseUpdateDto;
import it.academy.by.befitapp.dto.exercises.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.model.Exercise;
import it.academy.by.befitapp.service.api.IExercisesService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/profile/{id_profile}/journal/active")
public class ExercisesController {
    private final IExercisesService iExercisesService;

    public ExercisesController(IExercisesService iExercisesService) {
        this.iExercisesService = iExercisesService;
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@PathVariable("id_profile")Long id,
                                 @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                 @RequestParam(value = "size", required = false,defaultValue = "30")Integer size,
                                 @RequestParam(value = "dt_start",required = false) Long start,
                                 @RequestParam(value = "dt_end",required = false) Long end){
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
        ExerciseUpdateDto exerciseUpdateDto = new ExerciseUpdateDto();
        exerciseUpdateDto.setExercise(exercise);
        exerciseUpdateDto.setUpdateTime(ConvertTime.fromDateToMilli(exercise.getUpdateTime()));
        return new ResponseEntity<>(exerciseUpdateDto,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id_profile")Long id,
                                  @RequestBody Exercise exercise){
        Long  exerciseId= this.iExercisesService.save(exercise, id);
        return new ResponseEntity<>(exerciseId,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id_active}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id_profile")Long idProfile,
                                    @PathVariable("id_active")Long idActive,
                                    @PathVariable("dt_update")Long dtUpdate,
                                    @RequestBody Exercise exercise){
        this.iExercisesService.update(exercise,idProfile,idActive,dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_active}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id_profile")Long idProfile,
                                    @PathVariable("id_active")Long idActive,
                                    @PathVariable("dt_update")Long dtUpdate){
        this.iExercisesService.delete(idProfile,idActive,dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
