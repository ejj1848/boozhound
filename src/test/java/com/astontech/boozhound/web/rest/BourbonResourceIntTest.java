package com.astontech.boozhound.web.rest;

import com.astontech.boozhound.BoozhoundApp;
import com.astontech.boozhound.domain.Bourbon;
import com.astontech.boozhound.repository.BourbonRepository;
import com.astontech.boozhound.service.BourbonService;
import com.astontech.boozhound.repository.search.BourbonSearchRepository;

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
 * Test class for the BourbonResource REST controller.
 *
 * @see BourbonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoozhoundApp.class)
public class BourbonResourceIntTest {
    private static final String DEFAULT_BOURBON_NAME = "AAAAA";
    private static final String UPDATED_BOURBON_NAME = "BBBBB";
    private static final String DEFAULT_PROOF = "AAAAA";
    private static final String UPDATED_PROOF = "BBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_BOURBON_RATING = 1;
    private static final Integer UPDATED_BOURBON_RATING = 2;

    @Inject
    private BourbonRepository bourbonRepository;

    @Inject
    private BourbonService bourbonService;

    @Inject
    private BourbonSearchRepository bourbonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBourbonMockMvc;

    private Bourbon bourbon;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BourbonResource bourbonResource = new BourbonResource();
        ReflectionTestUtils.setField(bourbonResource, "bourbonService", bourbonService);
        this.restBourbonMockMvc = MockMvcBuilders.standaloneSetup(bourbonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bourbon createEntity(EntityManager em) {
        Bourbon bourbon = new Bourbon();
        bourbon = new Bourbon()
                .bourbonName(DEFAULT_BOURBON_NAME)
                .proof(DEFAULT_PROOF)
                .year(DEFAULT_YEAR)
                .bourbonRating(DEFAULT_BOURBON_RATING);
        return bourbon;
    }

    @Before
    public void initTest() {
        bourbonSearchRepository.deleteAll();
        bourbon = createEntity(em);
    }

    @Test
    @Transactional
    public void createBourbon() throws Exception {
        int databaseSizeBeforeCreate = bourbonRepository.findAll().size();

        // Create the Bourbon

        restBourbonMockMvc.perform(post("/api/bourbons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourbon)))
                .andExpect(status().isCreated());

        // Validate the Bourbon in the database
        List<Bourbon> bourbons = bourbonRepository.findAll();
        assertThat(bourbons).hasSize(databaseSizeBeforeCreate + 1);
        Bourbon testBourbon = bourbons.get(bourbons.size() - 1);
        assertThat(testBourbon.getBourbonName()).isEqualTo(DEFAULT_BOURBON_NAME);
        assertThat(testBourbon.getProof()).isEqualTo(DEFAULT_PROOF);
        assertThat(testBourbon.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBourbon.getBourbonRating()).isEqualTo(DEFAULT_BOURBON_RATING);

        // Validate the Bourbon in ElasticSearch
        Bourbon bourbonEs = bourbonSearchRepository.findOne(testBourbon.getId());
        assertThat(bourbonEs).isEqualToComparingFieldByField(testBourbon);
    }

    @Test
    @Transactional
    public void checkBourbonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bourbonRepository.findAll().size();
        // set the field null
        bourbon.setBourbonName(null);

        // Create the Bourbon, which fails.

        restBourbonMockMvc.perform(post("/api/bourbons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourbon)))
                .andExpect(status().isBadRequest());

        List<Bourbon> bourbons = bourbonRepository.findAll();
        assertThat(bourbons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBourbons() throws Exception {
        // Initialize the database
        bourbonRepository.saveAndFlush(bourbon);

        // Get all the bourbons
        restBourbonMockMvc.perform(get("/api/bourbons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bourbon.getId().intValue())))
                .andExpect(jsonPath("$.[*].bourbonName").value(hasItem(DEFAULT_BOURBON_NAME.toString())))
                .andExpect(jsonPath("$.[*].proof").value(hasItem(DEFAULT_PROOF.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].bourbonRating").value(hasItem(DEFAULT_BOURBON_RATING)));
    }

    @Test
    @Transactional
    public void getBourbon() throws Exception {
        // Initialize the database
        bourbonRepository.saveAndFlush(bourbon);

        // Get the bourbon
        restBourbonMockMvc.perform(get("/api/bourbons/{id}", bourbon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bourbon.getId().intValue()))
            .andExpect(jsonPath("$.bourbonName").value(DEFAULT_BOURBON_NAME.toString()))
            .andExpect(jsonPath("$.proof").value(DEFAULT_PROOF.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.bourbonRating").value(DEFAULT_BOURBON_RATING));
    }

    @Test
    @Transactional
    public void getNonExistingBourbon() throws Exception {
        // Get the bourbon
        restBourbonMockMvc.perform(get("/api/bourbons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBourbon() throws Exception {
        // Initialize the database
        bourbonService.save(bourbon);

        int databaseSizeBeforeUpdate = bourbonRepository.findAll().size();

        // Update the bourbon
        Bourbon updatedBourbon = bourbonRepository.findOne(bourbon.getId());
        updatedBourbon
                .bourbonName(UPDATED_BOURBON_NAME)
                .proof(UPDATED_PROOF)
                .year(UPDATED_YEAR)
                .bourbonRating(UPDATED_BOURBON_RATING);

        restBourbonMockMvc.perform(put("/api/bourbons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBourbon)))
                .andExpect(status().isOk());

        // Validate the Bourbon in the database
        List<Bourbon> bourbons = bourbonRepository.findAll();
        assertThat(bourbons).hasSize(databaseSizeBeforeUpdate);
        Bourbon testBourbon = bourbons.get(bourbons.size() - 1);
        assertThat(testBourbon.getBourbonName()).isEqualTo(UPDATED_BOURBON_NAME);
        assertThat(testBourbon.getProof()).isEqualTo(UPDATED_PROOF);
        assertThat(testBourbon.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBourbon.getBourbonRating()).isEqualTo(UPDATED_BOURBON_RATING);

        // Validate the Bourbon in ElasticSearch
        Bourbon bourbonEs = bourbonSearchRepository.findOne(testBourbon.getId());
        assertThat(bourbonEs).isEqualToComparingFieldByField(testBourbon);
    }

    @Test
    @Transactional
    public void deleteBourbon() throws Exception {
        // Initialize the database
        bourbonService.save(bourbon);

        int databaseSizeBeforeDelete = bourbonRepository.findAll().size();

        // Get the bourbon
        restBourbonMockMvc.perform(delete("/api/bourbons/{id}", bourbon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bourbonExistsInEs = bourbonSearchRepository.exists(bourbon.getId());
        assertThat(bourbonExistsInEs).isFalse();

        // Validate the database is empty
        List<Bourbon> bourbons = bourbonRepository.findAll();
        assertThat(bourbons).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBourbon() throws Exception {
        // Initialize the database
        bourbonService.save(bourbon);

        // Search the bourbon
        restBourbonMockMvc.perform(get("/api/_search/bourbons?query=id:" + bourbon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bourbon.getId().intValue())))
            .andExpect(jsonPath("$.[*].bourbonName").value(hasItem(DEFAULT_BOURBON_NAME.toString())))
            .andExpect(jsonPath("$.[*].proof").value(hasItem(DEFAULT_PROOF.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].bourbonRating").value(hasItem(DEFAULT_BOURBON_RATING)));
    }
}
