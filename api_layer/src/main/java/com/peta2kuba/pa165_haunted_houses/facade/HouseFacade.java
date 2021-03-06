package com.peta2kuba.pa165_haunted_houses.facade;

import com.peta2kuba.pa165_haunted_houses.dto.HaunterDTO;
import com.peta2kuba.pa165_haunted_houses.dto.HouseDTO;
import java.sql.Time;

import java.util.List;

/**
 * @author turcovsky on 28/10/15.
 */
public interface HouseFacade {

    void createHouse(HouseDTO houseDTO);

    void editHouse(HouseDTO houseDTO);

    void removeHouse(HouseDTO houseDTO);
    
    void removeHouseById(Long id);

    HouseDTO findById(Long id);

    List<HouseDTO> findAll();

    /**
     * Try to exorcise specific haunter, in specific house, in specific time.
     *
     * @param houseDTO specific house
     * @param exorcismTime specific time
     * @return true if haunter was successfully exorcised (at the right time in
     * the right place). False otherwise.
     */
    boolean exorcism(HouseDTO houseDTO, Time exorcismTime);
}
