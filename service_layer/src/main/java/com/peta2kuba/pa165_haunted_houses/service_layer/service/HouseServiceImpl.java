package com.peta2kuba.pa165_haunted_houses.service_layer.service;

import com.peta2kuba.pa165_haunted_houses.dao.HaunterDao;
import com.peta2kuba.pa165_haunted_houses.dao.HouseDao;
import com.peta2kuba.pa165_haunted_houses.entity.Haunter;
import com.peta2kuba.pa165_haunted_houses.entity.HauntingHours;
import com.peta2kuba.pa165_haunted_houses.entity.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

/**
 *
 * @author skornok on 24/11/15.
 */
@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private HaunterDao haunterDao;

    @Override
    public void createHouse(final House house) {
        houseDao.create(house);
    }

    @Override
    public void editHouse(final House house) {
        houseDao.edit(house);
    }

    @Override
    public void removeHouse(final House house) {
        houseDao.remove(house);
    }

    @Override
    public House findById(final Long id) {
        return houseDao.findById(id);
    }

    @Override
    public List<House> findAll() {
        return houseDao.findAll();
    }

    @Override
    public boolean exorcism(final House house, Time exorcismTime) {
        if (house != null && exorcismTime != null) {
            Haunter haunter = house.getHaunter();
            if (haunter != null) {
                HauntingHours hh = haunter.getHauntingHours();

                // If I'm performing exorcism when the haunter is present, kill it! Kill it with fire!!!
                if (hh.getFromTime().before(hh.getToTime())) {
                    if (exorcismTime.after(hh.getFromTime()) && exorcismTime.before(hh.getToTime())) {
                        return removeHaunter(haunter);
                    }
                } else {
                    if (!(exorcismTime.before(hh.getFromTime()) && exorcismTime.after(hh.getToTime()))) {
                        return removeHaunter(haunter);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Remove haunter from database
     * @param h haunter to remove
     * @return true, haunter was removed
     */
    private boolean removeHaunter(Haunter h) {
        haunterDao.remove(h);
        return true;
    }
}
