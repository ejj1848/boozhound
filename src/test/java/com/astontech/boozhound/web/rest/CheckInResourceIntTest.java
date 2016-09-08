package com.astontech.boozhound.web.rest;

import com.astontech.boozhound.BoozhoundApp;
import com.astontech.boozhound.domain.CheckIn;
import com.astontech.boozhound.repository.CheckInRepository;
import com.astontech.boozhound.service.CheckInService;
import com.astontech.boozhound.repository.search.CheckInSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CheckInResource REST controller.
 *
 * @see CheckInResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoozhoundApp.class)
public class CheckInResourceIntTest {
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private CheckInRepository checkInRepository;

    @Inject
    private CheckInService checkInService;

    @Inject
    private CheckInSearchRepository checkInSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCheckInMockMvc;

    private CheckIn checkIn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CheckInResource checkInResource = new CheckInResource();
        ReflectionTestUtils.setField(checkInResource, "checkInService", checkInService);
        this.restCheckInMockMvc = MockMvcBuilders.standaloneSetup(checkInResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckIn createEntity(EntityManager em) {
        CheckIn checkIn = new CheckIn();
        checkIn = new CheckIn()
                .notes(DEFAULT_NOTES);
        return checkIn;
    }

    @Before
    public void initTest() {
        checkInSearchRepository.deleteAll();
        checkIn = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckIn() throws Exception {
        int databaseSizeBeforeCreate = checkInRepository.findAll().size();

        // Create the CheckIn

        restCheckInMockMvc.perform(post("/api/check-ins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(checkIn)))
                .andExpect(status().isCreated());

        // Validate the CheckIn in the database
        List<CheckIn> checkIns = checkInRepository.findAll();
        assertThat(checkIns).hasSize(databaseSizeBeforeCreate + 1);
        CheckIn testCheckIn = checkIns.get(checkIns.size() - 1);
        assertThat(testCheckIn.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the CheckIn in ElasticSearch
        CheckIn checkInEs = checkInSearchRepository.findOne(testCheckIn.getId());
        assertThat(checkInEs).isEqualToComparingFieldByField(testCheckIn);
    }

    @Test
    @Transactional
    public void getAllCheckIns() throws Exception {
        // Initialize the database
        checkInRepository.saveAndFlush(checkIn);

        // Get all the checkIns
        restCheckInMockMvc.perform(get("/api/check-ins?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(checkIn.getId().intValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getCheckIn() throws Exception {
        // Initialize the database
        checkInRepository.saveAndFlush(checkIn);

        // Get the checkIn
        restCheckInMockMvc.perform(get("/api/check-ins/{id}", checkIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checkIn.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheckIn() throws Exception {
        // Get the checkIn
        restCheckInMockMvc.perform(get("/api/check-ins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckIn() throws Exception {
        // Initialize the database
        checkInService.save(checkIn);

        int databaseSizeBeforeUpdate = checkInRepository.findAll().size();

        // Update the checkIn
        CheckIn updatedCheckIn = checkInRepository.findOne(checkIn.getId());
        updatedCheckIn
                .notes(UPDATED_NOTES);

        restCheckInMockMvc.perform(put("/api/check-ins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCheckIn)))
                .andExpect(status().isOk());

        // Validate the CheckIn in the database
        List<CheckIn> checkIns = checkInRepository.findAll();
        assertThat(checkIns).hasSize(databaseSizeBeforeUpdate);
        CheckIn testCheckIn = checkIns.get(checkIns.size() - 1);
        assertThat(testCheckIn.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the CheckIn in ElasticSearch
        CheckIn checkInEs = checkInSearchRepository.findOne(testCheckIn.getId());
        assertThat(checkInEs).isEqualToComparingFieldByField(testCheckIn);
    }

    @Test
    @Transactional
    public void deleteCheckIn() throws Exception {
        // Initialize the database
        checkInService.save(checkIn);

        int databaseSizeBeforeDelete = checkInRepository.findAll().size();

        // Get the checkIn
        restCheckInMockMvc.perform(delete("/api/check-ins/{id}", checkIn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean checkInExistsInEs = checkInSearchRepository.exists(checkIn.getId());
        assertThat(checkInExistsInEs).isFalse();

        // Validate the database is empty
        List<CheckIn> checkIns = checkInRepository.findAll();
        assertThat(checkIns).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCheckIn() throws Exception {
        // Initialize the database
        checkInService.save(checkIn);

        // Search the checkIn
        restCheckInMockMvc.perform(get("/api/_search/check-ins?query=id:" + checkIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkIn.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }
}
