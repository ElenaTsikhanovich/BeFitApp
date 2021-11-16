package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.ExercisesAndWeightSearchDto;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.WeightMeasurement;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/profile/{id_profile}/journal/weight")
public class WeightMeasurementController {
    private final IWeightMeasurementService iWeightMeasurementService;

    public WeightMeasurementController(IWeightMeasurementService iWeightMeasurementService) {
        this.iWeightMeasurementService = iWeightMeasurementService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@PathVariable("id_profile") Long idProfile,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "30") Integer size,
                                    @RequestParam(value = "dt_start", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                    @RequestParam(value = "dt_end", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        ExercisesAndWeightSearchDto exercisesAndWeightSearchDto = new ExercisesAndWeightSearchDto();
        exercisesAndWeightSearchDto.setPage(page);
        exercisesAndWeightSearchDto.setSize(size);
        exercisesAndWeightSearchDto.setStart(start);
        exercisesAndWeightSearchDto.setEnd(end);
        Page<WeightMeasurement> weightMeasurementsPage = this.iWeightMeasurementService.getAll(idProfile, exercisesAndWeightSearchDto);
        List<WeightMeasurement> weightMeasurements = weightMeasurementsPage.getContent();
        return new ResponseEntity<>(weightMeasurements, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id_weight}")
    public ResponseEntity<?> get(@PathVariable("id_profile") Long idProfile,
                                 @PathVariable("id_weight") Long idWeight) {
        WeightMeasurement weightMeasurement = this.iWeightMeasurementService.get(idProfile, idWeight);
        return new ResponseEntity<>(weightMeasurement, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id_profile") Long id,
                                  @RequestBody WeightMeasurement weightMeasurement) {
        Long weightMeasurementId = this.iWeightMeasurementService.save(weightMeasurement, id);
        return new ResponseEntity<>(weightMeasurementId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id_weight}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable("id_profile") Long idProfile,
                                    @PathVariable("id_weight")Long idWeight,
                                    @PathVariable("dt_update")Long dtUpdate,
                                    @RequestBody WeightMeasurement weightMeasurement) {
        this.iWeightMeasurementService.update(weightMeasurement, idProfile, idWeight);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id_weight}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@PathVariable("id_profile") Long idProfile,
                                    @PathVariable("id_weight")Long idWeight,
                                    @PathVariable("dt_update")Long dtUpdate) {
        this.iWeightMeasurementService.delete(idWeight);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
//переписать апдейты и делиты
//сделать правильные урлы