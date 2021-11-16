package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IJournalDao;
import it.academy.by.befitapp.dto.JournalSearchDto;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Profile;
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
    private final IProfileService iProfileService;

    public JournalService(IJournalDao iJournalDao, IProfileService iProfileService) {
        this.iJournalDao = iJournalDao;
        this.iProfileService = iProfileService;
    }

    @Override
    public Journal get(Long idProfile,Long idFood) {
        //проверка взять этот профайл на возможность видимости и если нет то идет в юзер холдер
        Journal byProfileIdAndId = this.iJournalDao.findJournalByProfileIdAndId(idProfile, idFood);
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
        return saveDairyId;
    }

    //переписать апдейт и делит
    @Override
    public void update(Journal dairy, Long idProfile, Long idFood) {
        Journal dairyFromBd = get(idProfile, idFood);
        dairy.setId(idFood);
        dairy.setCreateTime(dairyFromBd.getCreateTime());
        dairy.setUpdateTime(LocalDateTime.now());
        Profile profile = this.iProfileService.get(idProfile);
        dairy.setProfile(profile);
        this.iJournalDao.save(dairy);

    }

    @Override
    public void delete(Long id) {
        this.iJournalDao.deleteById(id);
    }
}
