package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IJournalDao;
import it.academy.by.befitapp.dto.JournalSearchDto;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IJournalService;
import it.academy.by.befitapp.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JournalService implements IJournalService {
    private final IJournalDao iJournalDao;
    private final IAuditService iAuditService;
    private final IProfileService iProfileService;

    public JournalService(IJournalDao iJournalDao, IAuditService iAuditService, IProfileService iProfileService) {
        this.iJournalDao = iJournalDao;
        this.iAuditService = iAuditService;
        this.iProfileService = iProfileService;
    }

    @Override
    public Journal get(Long idProfile,JournalSearchDto journalSearchDto) {
        Journal byProfileIdAndId = this.iJournalDao.findByProfileIdAndId(idProfile, journalSearchDto.getIdFood());
        return byProfileIdAndId;
    }

    @Override
    public Page<Journal> getAll(Long idProfile, JournalSearchDto journalSearchDto) {
        if(journalSearchDto.getDay()!=null){
            return null;//метод котоый находит по дню все
        }
        Pageable pageable= PageRequest.of(journalSearchDto.getPage(), journalSearchDto.getSize());
        return this.iJournalDao.findAllByProfileId(idProfile, pageable);
    }



    @Override
    public Long save(Journal dairy, Long id) {
        LocalDateTime createTime = LocalDateTime.now();
        dairy.setCreateTime(createTime);
        dairy.setUpdateTime(createTime);
        Profile profile = this.iProfileService.get(id);
        dairy.setProfile(profile);
        Journal saveDairy = this.iJournalDao.save(dairy);
        Long saveDairyId = saveDairy.getId();
        this.iAuditService.save(EAuditAction.SAVE, EntityType.DAIRY, id);
        return saveDairyId;
    }

    //переписать апдейт и делит
    @Override
    public void update(Journal dairy, Long id) {
        Journal dairyForUpdate = this.iJournalDao.findById(id).get();
        dairyForUpdate.setEatingTime(dairy.getEatingTime());
        dairyForUpdate.setProduct(dairy.getProduct());
        dairyForUpdate.setDish(dairy.getDish());
        dairyForUpdate.setWeight(dairy.getWeight());
        LocalDateTime updateTime = LocalDateTime.now();
        dairyForUpdate.setUpdateTime(updateTime);
        this.iJournalDao.save(dairyForUpdate);
        this.iAuditService.save(EAuditAction.UPDATE, EntityType.DAIRY, id);
    }

    @Override
    public void delete(Long id) {
        this.iJournalDao.deleteById(id);
        this.iAuditService.save(EAuditAction.DELETE, EntityType.DAIRY, id);
    }
}
