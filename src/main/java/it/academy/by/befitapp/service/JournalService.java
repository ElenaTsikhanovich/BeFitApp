package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IJournalDao;
import it.academy.by.befitapp.dto.journal.JournalSearchDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.NoRightsForChangeException;
import it.academy.by.befitapp.exception.UpdateDeleteException;
import it.academy.by.befitapp.model.Dish;
import it.academy.by.befitapp.model.Journal;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.service.api.IDishService;
import it.academy.by.befitapp.service.api.IJournalService;
import it.academy.by.befitapp.service.api.IProductService;
import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.utils.ConvertTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class JournalService implements IJournalService {
    private final IJournalDao iJournalDao;
    private final IProfileService iProfileService;
    private final IProductService iProductService;
    private final IDishService iDishService;

    public JournalService(IJournalDao iJournalDao, IProfileService iProfileService, IProductService iProductService,
                          IDishService iDishService) {
        this.iJournalDao = iJournalDao;
        this.iProfileService = iProfileService;
        this.iProductService = iProductService;
        this.iDishService = iDishService;
    }

    @Override
    public Journal get(Long idProfile,Long idFood) {
        Journal byProfileIdAndId = this.iJournalDao.findJournalByProfileIdAndId(idProfile, idFood);
        if (byProfileIdAndId==null){
            throw new ElementNotFoundException();
        }
        return byProfileIdAndId;
    }

    @Override
    public Page<Journal> getAll(Long idProfile, JournalSearchDto journalSearchDto) {
        Pageable pageable= PageRequest.of(journalSearchDto.getPage(), journalSearchDto.getSize());
        if(journalSearchDto.getDay()!=null){
            LocalDateTime dayStart = ConvertTime.fromMilliToDate(journalSearchDto.getDay());
            LocalDateTime dayEnd = dayStart.plusDays(1);
            Page<Journal> allByProfileIdAndUpdateTimeBetween =
                    this.iJournalDao.findAllByProfileIdAndUpdateTimeBetween(idProfile, dayStart, dayEnd,pageable);
            return allByProfileIdAndUpdateTimeBetween;
        }
        return this.iJournalDao.findAllByProfileId(idProfile, pageable);
    }

    @Override
    public Long save(Journal dairy, Long id) {
        if (this.iProfileService.checkCurrentUser(id)) {
            LocalDateTime createTime = LocalDateTime.now();
            dairy.setCreateTime(createTime);
            dairy.setUpdateTime(createTime);
            Profile profile = this.iProfileService.get(id);
            dairy.setProfile(profile);
            if (dairy.getProduct()!=null){
                Product product = this.iProductService.get(dairy.getProduct().getId());
                dairy.setProduct(product);
            }
            if (dairy.getDish()!=null){
                Dish dish = this.iDishService.get(dairy.getDish().getId());
                dairy.setDish(dish);
            }
            Journal saveDairy = this.iJournalDao.save(dairy);
            Long saveDairyId = saveDairy.getId();
            return saveDairyId;
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void update(Journal dairy, Long idProfile, Long idFood,Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            Journal dairyFromBd = get(idProfile, idFood);
            dairyFromBd.setEatingTime(dairy.getEatingTime());
            dairyFromBd.setWeight(dairy.getWeight());
            if (dairy.getProduct()!=null){
                Product product = this.iProductService.get(dairy.getProduct().getId());
                dairy.setProduct(product);
            }
            if (dairy.getDish()!=null){
                Dish dish = this.iDishService.get(dairy.getDish().getId());
                dairy.setDish(dish);
            }
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(dairyFromBd.getUpdateTime()))) {
                this.iJournalDao.save(dairy);
            } else {
                throw new UpdateDeleteException();
            }
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void delete(Long idProfile,Long idFood,Long dtUpdate) {
        if (this.iProfileService.checkCurrentUser(idProfile)) {
            Journal journal = get(idProfile, idFood);
            if (Objects.equals(dtUpdate, ConvertTime.fromDateToMilli(journal.getUpdateTime()))) {
                this.iJournalDao.deleteById(idFood);
            } else {
                throw new UpdateDeleteException();
            }
        }else {
            throw new NoRightsForChangeException();
        }
    }

}
