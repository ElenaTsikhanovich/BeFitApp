package it.academy.by.befitapp.service;

import it.academy.by.befitapp.dao.api.IProfileDao;
import it.academy.by.befitapp.dto.ListDto;
import it.academy.by.befitapp.dto.LoginDto;
import it.academy.by.befitapp.model.Profile;
import it.academy.by.befitapp.model.User;
import it.academy.by.befitapp.model.api.EAuditAction;
import it.academy.by.befitapp.model.api.EntityType;
import it.academy.by.befitapp.security.UserHolder;
import it.academy.by.befitapp.service.api.IAuditService;
import it.academy.by.befitapp.service.api.IProfileService;
import it.academy.by.befitapp.service.api.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileService implements IProfileService {
    private final IProfileDao iProfileDao;
    private final UserHolder userHolder;
    private final IUserService iUserService;

    public ProfileService(IProfileDao iProfileDao, UserHolder userHolder, IUserService iUserService) {
        this.iProfileDao = iProfileDao;
        this.userHolder = userHolder;
        this.iUserService = iUserService;
    }

    @Override
    public Profile get(Long id) {
        Profile profile = this.iProfileDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Нет данных с таким id"));
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
        User userByLogin = this.iUserService.getByLogin(userLogin); //как варриант из холдера возвращать сразу юзера целого
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
        Profile profileFromBd = get(id);
        profile.setId(id);
        profile.setCreateTime(profileFromBd.getCreateTime());
        profile.setUpdateTime(LocalDateTime.now());
        profile.setUser(profileFromBd.getUser());
        this.iProfileDao.save(profile);
    }

    @Override
    public void delete(Long id) {
        this.iProfileDao.deleteById(id);
    }
}
