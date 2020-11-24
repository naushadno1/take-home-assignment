package com.rhdhv.assignment.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhdhv.assignment.controllers.impl.CarDealerController;
import com.rhdhv.assignment.models.Car;
import com.rhdhv.assignment.models.NumberSearch;
import com.rhdhv.assignment.models.NumberSearch.NumberComparison;
import com.rhdhv.assignment.models.Search;
import com.rhdhv.assignment.models.StringSearch;
import com.rhdhv.assignment.models.StringSearch.StringComparison;
import com.rhdhv.assignment.services.ICarDealerService;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
@WebMvcTest(controllers = CarDealerController.class)
public class CarDealerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ICarDealerService carDealerService;

  @Test
  public void testWhenSearchAllCarsThen200OK() throws Exception {
    mockMvc.perform(post("/cars/search").contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  public void testWhenSearchCarsByYearThen200OK() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("yearOfRelease",
        NumberSearch.builder().type("number").comparison(NumberComparison.eq).value(2000).build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isOk());
  }

  @Test
  public void testWhenSearchCarsByMakeThen200OK() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("brand",
        StringSearch.builder().type("string").comparison(StringComparison.eq).value("Honda")
            .build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isOk());
  }

  @Test
  public void testWhenSearchCarsNoTypeThen400BADREQUEST() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("brand",
        StringSearch.builder().comparison(StringComparison.eq).value("Honda")
            .build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isBadRequest());
  }

  @Test
  public void testWhenSearchCarsNoValueThen400BADREQUEST() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("brand",
        StringSearch.builder().type("string").comparison(StringComparison.eq)
            .build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isBadRequest());
  }

  @Test
  public void testWhenSearchCarsNoComparisonThen400BADREQUEST() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("brand",
        StringSearch.builder().type("string").value("abc")
            .build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isBadRequest());
  }

  @Test
  public void testWhenSearchCarsByMakeAndYearThen200OK() throws Exception {
    Map<String, Search> map = new HashMap<>();
    map.put("brand",
        StringSearch.builder().type("string").comparison(StringComparison.eq).value("Honda")
            .build());
    map.put("yearOfRelease",
        NumberSearch.builder().type("number").comparison(NumberComparison.eq).value(2000).build());
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(map))).andExpect(status().isOk());
  }

  @Test
  public void testWhenWrongAPICallThen200OK() throws Exception {
    mockMvc.perform(get("/cars/api?brand=Honda&yearOfRelease=2000"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testWhenPostCarsCallThen201CREATED() throws Exception {
    Car expectedRecord = Car.builder().brand("Honda").model("Cr-V").version("c").yearOfRelease(2019)
        .price(1234).fuelConsumption(0).maintenanceCost(1).build();
    mockMvc.perform(post("/cars")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(expectedRecord)))
        .andExpect(status().isCreated());
  }

  @Test
  public void testWhenPostCarsCallOnlyMandatoryFieldsThen200CREATED() throws Exception {
    Car expectedRecord = Car.builder().brand("Honda").model("Cr-V").version("c").yearOfRelease(2019)
        .price(1234).build();
    mockMvc.perform(post("/cars")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(expectedRecord)))
        .andExpect(status().isCreated());
  }

  @Test
  public void testWhenPostCarsCallMissingMandatoryFieldThen400BADREQUEST() throws Exception {
    Car expectedRecord = Car.builder().model("Cr-V").version("c").yearOfRelease(2019).price(1234)
        .build();
    mockMvc.perform(post("/cars")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(expectedRecord)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testWhenPostCarsCallEmptyMandatoryFieldThen400BADREQUEST() throws Exception {
    Car expectedRecord = Car.builder().brand("").model("Cr-V").version("c").yearOfRelease(2019)
        .price(1234).build();
    mockMvc.perform(post("/cars")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(expectedRecord)))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testWhenPost1CarsCallThen201CREATED() throws Exception {
    Car expectedRecord = Car.builder().brand("Honda").model("Cr-V").version("c").yearOfRelease(2019)
        .price(1234).fuelConsumption(0).maintenanceCost(1).build();
    mockMvc.perform(post("/cars/search")
        .contentType("application/json")
    )
        .andExpect(status().isOk());
  }
}
