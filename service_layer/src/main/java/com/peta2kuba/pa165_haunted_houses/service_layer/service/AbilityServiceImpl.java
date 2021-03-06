package com.peta2kuba.pa165_haunted_houses.service_layer.service;

import com.peta2kuba.pa165_haunted_houses.dao.AbilityDao;
import com.peta2kuba.pa165_haunted_houses.entity.Ability;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author petr.melicherik
 */
@Service
public class AbilityServiceImpl implements AbilityService {

    @Autowired
    private AbilityDao abilityDao;

    @Override
    public void createAbility(Ability ability) {
        abilityDao.create(ability);
    }

    @Override
    public void editAbility(Ability ability) {
        abilityDao.edit(ability);
    }

    @Override
    public void removeAbility(Ability ability) {
        Ability a = abilityDao.findById(ability.getId());
        abilityDao.remove(a);
    }

    @Override
    public Ability findAbilityById(Long id) {
        return abilityDao.findById(id);
    }

    @Override
    public List<Ability> findAllAbilities() {
        return abilityDao.findAll();
    }

}
