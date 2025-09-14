package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.repository.DogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateDog() throws Exception {
        Dog dog = new Dog();
        dog.setName("Rex");
        dog.setAge(3);
        dog.setBreed("beagle");

        mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub("test-user-f"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.breed").value("beagle"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.dogID").isNumber());
    }

    @Test
    public void shouldGetDogById() throws Exception {
        Dog dog = new Dog();
        dog.setName("Rex");
        dog.setAge(3);
        dog.setBreed("beagle");
        Dog savedDog = dogRepository.save(dog);

        mockMvc.perform(get("/api/v1/dogs/{id}", savedDog.getDogID())
                        .with(jwtWithSub("test-user-e"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dogID").value(savedDog.getDogID()))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.breed").value("beagle"));
    }

    @Test
    public void shouldGetAllDogs() throws Exception {
        Dog dog1 = new Dog();
        dog1.setName("Rex");
        dog1.setAge(3);
        dog1.setBreed("beagle");

        Dog dog2 = new Dog();
        dog2.setName("Max");
        dog2.setAge(5);
        dog2.setBreed("bulldog");

        dogRepository.save(dog1);
        dogRepository.save(dog2);

        mockMvc.perform(get("/api/v1/dogs")
                        .with(jwtWithSub("test-user-d"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].age").value(3))
                .andExpect(jsonPath("$[0].breed").value("beagle"))
                .andExpect(jsonPath("$[1].name").value("Max"))
                .andExpect(jsonPath("$[1].age").value(5))
                .andExpect(jsonPath("$[1].breed").value("bulldog"));
    }

    @Test
    public void shouldGetDogsByOwner() throws Exception {
        // create as test-user-c (owner will be set by service)
        Dog rex = new Dog();
        rex.setName("Rex");
        rex.setAge(3);
        rex.setBreed("beagle");

        Dog max = new Dog();
        max.setName("Max");
        max.setAge(5);
        max.setBreed("bulldog");

        mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub("test-user-c"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rex)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub("test-user-c"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(max)))
                .andExpect(status().isOk());

        // query for dogs of test-user-c
        mockMvc.perform(get("/api/v1/dogs/owner/{id}", "test-user-c")
                        .with(jwtWithSub("test-user-c"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("Rex", "Max")))
                .andExpect(jsonPath("$[*].breed").value(containsInAnyOrder("beagle", "bulldog")))
                .andExpect(jsonPath("$[*].age").value(containsInAnyOrder(3, 5)));
    }

    @Test
    public void shouldUpdateDog() throws Exception {
        Dog toCreate = new Dog();
        toCreate.setName("Rex");
        toCreate.setAge(3);
        toCreate.setBreed("beagle");

        String createdJson = mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub("test-user-b"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Dog created = objectMapper.readValue(createdJson, Dog.class);
        Integer id = created.getDogID();

        Dog updatePayload = new Dog();
        updatePayload.setName("Buddy");
        updatePayload.setAge(4);
        updatePayload.setBreed("corgi");

        mockMvc.perform(put("/api/v1/dogs/{id}", id)
                        .with(jwtWithSub("test-user-b"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dogID").value(id))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.age").value(4))
                .andExpect(jsonPath("$.breed").value("corgi"));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingDog() throws Exception {
        Integer nonExistingId = 999_999;
        Dog updatePayload = new Dog();
        updatePayload.setName("Ghost");
        updatePayload.setAge(10);
        updatePayload.setBreed("unknown");

        mockMvc.perform(put("/api/v1/dogs/{id}", nonExistingId)
                        .with(jwtWithSub("test-user-a"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Dog not found with id: " + nonExistingId)));
    }

    @Test
    public void shouldChangeOwner() throws Exception {
        String userA = "user-A";

        Dog toCreate = new Dog();
        toCreate.setName("Rex");
        toCreate.setAge(3);
        toCreate.setBreed("beagle");

        String createdJson = mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub(userA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Dog created = objectMapper.readValue(createdJson, Dog.class);
        Integer dogId = created.getDogID();

        String userB = "user-B";

        mockMvc.perform(put("/api/v1/dogs/change-owner/{dogID}", dogId)
                        .with(jwtWithSub(userB))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dogID").value(dogId))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.breed").value("beagle"));

        Dog reloaded = dogRepository.findById(dogId).orElseThrow();
        assertThat(reloaded.getOwner()).isNotNull();
        assertThat(reloaded.getOwner().getUserID()).isEqualTo(userB);
    }

    @Test
    public void shouldReturnNotFound_whenChangeOwnerOfNonExistingDog() throws Exception {
        int nonExistingId = 123456789;

        mockMvc.perform(put("/api/v1/dogs/change-owner/{dogID}", nonExistingId)
                        .with(jwtWithSub("some-user"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Dog not found with id: " + nonExistingId)));
    }

    @Test
    public void shouldDeleteDog() throws Exception {
        Dog toCreate = new Dog();
        toCreate.setName("ToDelete");
        toCreate.setAge(2);
        toCreate.setBreed("mix");

        String createdJson = mockMvc.perform(post("/api/v1/dogs")
                        .with(jwtWithSub("deleter-user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Dog created = objectMapper.readValue(createdJson, Dog.class);
        Integer dogId = created.getDogID();

        mockMvc.perform(delete("/api/v1/dogs/{id}", dogId)
                        .with(jwtWithSub("deleter-user")))
                .andExpect(status().isOk());

        assertThat(dogRepository.findById(dogId)).isEmpty();

        mockMvc.perform(get("/api/v1/dogs/{id}", dogId)
                        .with(jwtWithSub("deleter-user")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistingDog() throws Exception {
        int nonExistingId = 1_000_001;

        mockMvc.perform(delete("/api/v1/dogs/{id}", nonExistingId)
                        .with(jwtWithSub("deleter-user")))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Dog not found with id: " + nonExistingId)));
    }

    private RequestPostProcessor jwtWithSub(String sub) {
        return jwt()
                .jwt(j -> j.claim("sub", sub))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }

}