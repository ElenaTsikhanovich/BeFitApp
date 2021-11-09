package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IWeightMeasurementDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.WeightMeasurement;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;

import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.service.api.IWeightMeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeightMeasurementService implements IWeightMeasurementService {
    private final IWeightMeasurementDao iWeightMeasurementDao;
    private final IAuditService iAuditService;
    private final IProfileService iProfileService;

    public WeightMeasurementService(IWeightMeasurementDao iWeightMeasurementDao, IAuditService iAuditService, IProfileService iProfileService) {
        this.iWeightMeasurementDao = iWeightMeasurementDao;
        this.iAuditService = iAuditService;
        this.iProfileService = iProfileService;
    }

    @Override
    public Page<WeightMeasurement> get(Long id, ListDto listDto) {
        Pageable pageable= PageRequest.of(listDto.getPage(), listDto.getSize());
        Page<WeightMeasurement> allByProfileId = this.iWeightMeasurementDao.findAllByProfileId(id, pageable);
        return allByProfileId;
    }

    @Override//должно при сейве отправлять данные в профайл
    public Long save(WeightMeasurement weightMeasurement, Long id) {
        LocalDateTime createTime = LocalDateTime.now();
        weightMeasurement.setCreateTime(createTime);
        weightMeasurement.setUpdateTime(createTime);
        Profile profile = this.iProfileService.get(id);
        profile.setUpdateTime(createTime);
        profile.setWeightActual(weightMeasurement.getWeight());
        this.iProfileService.update(profile,id);
        weightMeasurement.setProfile(profile);
        WeightMeasurement saveWeightMeasurement = this.iWeightMeasurementDao.save(weightMeasurement);
        Long saveId = saveWeightMeasurement.getId();
        this.iAuditService.save(EAuditAction.SAVE,EntityType.WEIGHT_MEASUREMENT, saveId);
        return saveId;
    }

    @Override //переписать
    public void update(WeightMeasurement weightMeasurement, Long id) {
        WeightMeasurement weightMeasurementForUpdate = this.iWeightMeasurementDao.findById(id).get();
        weightMeasurementForUpdate.setWeight(weightMeasurement.getWeight());
        LocalDateTime updateTime = LocalDateTime.now();
        weightMeasurementForUpdate.setUpdateTime(updateTime);
        this.iWeightMeasurementDao.save(weightMeasurementForUpdate);
        this.iAuditService.save(EAuditAction.UPDATE, EntityType.WEIGHT_MEASUREMENT, id);
    }

    @Override
    public void delete(Long id) {
        this.iWeightMeasurementDao.deleteById(id);
        this.iAuditService.save(EAuditAction.DELETE, EntityType.WEIGHT_MEASUREMENT, id);
    }
}
