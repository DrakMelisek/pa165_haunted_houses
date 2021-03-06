package com.peta2kuba.pa165_haunted_houses.service_layer.facade;

import com.peta2kuba.pa165_haunted_houses.dao.HaunterDao;
import com.peta2kuba.pa165_haunted_houses.dao.HouseDao;
import com.peta2kuba.pa165_haunted_houses.dto.HaunterDTO;
import com.peta2kuba.pa165_haunted_houses.dto.HouseDTO;
import com.peta2kuba.pa165_haunted_houses.entity.Haunter;
import com.peta2kuba.pa165_haunted_houses.entity.HauntingHours;
import com.peta2kuba.pa165_haunted_houses.entity.House;
import com.peta2kuba.pa165_haunted_houses.facade.HouseFacade;
import com.peta2kuba.pa165_haunted_houses.service_layer.BeanMappingService;
import com.peta2kuba.pa165_haunted_houses.service_layer.config.ServiceConfiguration;
import com.peta2kuba.pa165_haunted_houses.service_layer.service.HouseService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This Test class is testing methods of {@link HouseFacadeImpl}
 *
 * @author turcovsky on 27/11/15.
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class HouseFacadeImplTest extends AbstractTransactionalTestNGSpringContextTests {

	@Mock
	private HouseDao houseDao;

	@Mock
	private HaunterDao haunterDao;

	@Autowired
	@InjectMocks
	private HouseService houseService;

	@Autowired
	private BeanMappingService mapper;

	@Autowired
	private HouseFacade houseFacade;

	private House house;
	private HouseDTO houseDTO;

	@BeforeClass
	public void initClass() {
		MockitoAnnotations.initMocks(this);
	}

	@BeforeMethod
	public void init() {
		house = new House();
		house.setName("Home sweet home");
		house.setDescription("The one and only");
		house.setAddress("Sokolnikova 1");
		house.setHauntedSince(Timestamp.valueOf(LocalDateTime.now()));

		houseDTO = mapper.mapTo(house, HouseDTO.class);
	}

	/**
	 * Test of create method
	 */
	@Test
	public void testCreateHouse() {
		houseFacade.createHouse(houseDTO);
		verify(houseDao).create(house);
	}

	/**
	 * Test of editing method
	 */
	@Test
	public void testEditHouse() {
		houseFacade.editHouse(houseDTO);
		verify(houseDao).edit(house);
	}

	/**
	 * Test of remove method
	 */
	@Test
	public void testRemoveHouse() {
		houseFacade.removeHouse(houseDTO);
		verify(houseDao).remove(house);
	}

	/**
	 * Test of findById() method
	 */
	@Test
	public void testFindById() {
		Mockito.when(houseDao.findById(any(Long.class))).thenReturn(house);
		HouseDTO hDTO = houseFacade.findById(0l);
		Assert.assertEquals(mapper.mapTo(house, HouseDTO.class), hDTO);
	}

	/**
	 * Test of findAll() method
	 */
	@Test
	public void testFindAll() {
		List<House> houses = new ArrayList<>();
		houses.add(house);

		when(houseDao.findAll()).thenReturn(houses);
		List<HouseDTO> houseDTOs = houseFacade.findAll();
		Assert.assertEquals(mapper.mapTo(houses, HouseDTO.class), houseDTOs);
	}

	/**
	 * Test of exorcism method
	 */
	@Test
	public void testExorcism() {
		HauntingHours hh = new HauntingHours();
		hh.setFromTime(Time.valueOf("8:00:00"));
		hh.setToTime(Time.valueOf("20:00:00"));

		Haunter haunter = new Haunter();
		haunter.setHauntingHours(hh);
		haunter.setName("Premek");
		haunter.setDescription("Haunting people with his enormous head");
		haunter.setHauntingReason("Because");

		house.setHaunter(haunter);

		HaunterDTO haunterDTO = mapper.mapTo(haunter, HaunterDTO.class);

		houseDTO.setHaunter(haunterDTO);

		boolean result = houseFacade.exorcism(houseDTO, Time.valueOf("10:00:00"));
		Assert.assertTrue(result);
		verify(haunterDao).remove(haunter);
	}
}