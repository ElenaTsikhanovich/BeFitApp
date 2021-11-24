package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IProfileDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.exception.ElementNotFoundException;
import it.academy.by.befitapp.exception.MoreThenOneProfileException;
import it.academy.by.befitapp.exception.NoRightsForChangeException;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.Role;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuthService;
import it.academy.by.befitapp.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ProfileService implements IProfileService {
    private final IProfileDao iProfileDao;
    private final UserHolder userHolder;
    private final IAuthService iAuthService;

    public ProfileService(IProfileDao iProfileDao, UserHolder userHolder, IAuthService iAuthService) {
        this.iProfileDao = iProfileDao;
        this.userHolder = userHolder;
        this.iAuthService = iAuthService;
    }

    @Override
    public Profile get(Long id) {
        Profile profile = this.iProfileDao.findById(id).orElseThrow(
                ElementNotFoundException::new);
        return profile;
    }

    @Override
    public Page<Profile> getAll(ListDto listDto) {
        Pageable pageable= PageRequest.of(listDto.getPage(), listDto.getSize());
        Page<Profile> profiles = this.iProfileDao.findAll(pageable);
        return profiles;
    }

    @Override
    public Long save(Profile profile) {
        String userLogin = this.userHolder.getAuthentication().getName();
        User userByLogin = this.iAuthService.getByLogin(userLogin);
        Profile profileByUserId = this.iProfileDao.findProfileByUserId(userByLogin.getId());
        if (profileByUserId!=null){
            throw new MoreThenOneProfileException();
        }
        profile.setUser(userByLogin);
        LocalDateTime createTime = LocalDateTime.now();
        profile.setCreateTime(createTime);
        profile.setUpdateTime(createTime);
        Profile saveProfile = this.iProfileDao.save(profile);
        Long id = saveProfile.getId();
        return id;
    }

    @Override
    public void update(Profile profile, Long id) {
        if (checkCurrentUser(id)) {
            Profile profileFromBd = get(id);
            profileFromBd.setHeight(profile.getHeight());
            profileFromBd.setDateOfBirth(profile.getDateOfBirth());
            profileFromBd.setGender(profile.getGender());
            profileFromBd.setLifeStyle(profile.getLifeStyle());
            profileFromBd.setWeightActual(profile.getWeightActual());
            profileFromBd.setWeightGoal(profile.getWeightGoal());
            profileFromBd.setWeightTarget(profile.getWeightTarget());
            this.iProfileDao.save(profileFromBd);
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public void delete(Long id) {
        if (checkCurrentUser(id)) {
            this.iProfileDao.deleteById(id);
        }else {
            throw new NoRightsForChangeException();
        }
    }

    @Override
    public boolean checkCurrentUser(Long idProfile) {
        String userLogin = this.userHolder.getAuthentication().getName();
        User currentUser = this.iAuthService.getByLogin(userLogin);
        Profile profile = get(idProfile);
        User profileUser = profile.getUser();
        return (Objects.equals(currentUser.getId(), profileUser.getId()));
    }

}
