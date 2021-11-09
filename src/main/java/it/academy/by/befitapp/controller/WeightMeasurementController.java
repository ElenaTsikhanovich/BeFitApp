package it.academy.by.befitapp.controller;

import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.WeightMeasurement;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profiles/{id}/weight")
public class WeightMeasurementController {
    private final IWeightMeasurementService iWeightMeasurementService;

    public WeightMeasurementController(IWeightMeasurementService iWeightMeasurementService) {
        this.iWeightMeasurementService = iWeightMeasurementService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id")Long id,
                                 @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
                                 @RequestParam(value = "size", required = false,defaultValue = "30")Integer size){
        ListDto listDto = new ListDto();
        listDto.setPage(page);
        listDto.setSize(size);
        Page<WeightMeasurement> weightMeasurements = this.iWeightMeasurementService.get(id, listDto);
        return new ResponseEntity<>(weightMeasurements, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable("id")Long id,
                                  @RequestBody WeightMeasurement weightMeasurement){
        Long weightMeasurementId = this.iWeightMeasurementService.save(weightMeasurement, id);
        return new ResponseEntity<>(weightMeasurementId, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody WeightMeasurement weightMeasurement){
        this.iWeightMeasurementService.update(weightMeasurement,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.iWeightMeasurementService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
//переписать апдейты и делиты