package com.astontech.boozhound.web.rest;

import com.astontech.boozhound.BoozhoundApp;
import com.astontech.boozhound.domain.Distiller;
import com.astontech.boozhound.repository.DistillerRepository;
import com.astontech.boozhound.service.DistillerService;
import com.astontech.boozhound.repository.search.DistillerSearchRepository;

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
 * Test class for the DistillerResource REST controller.
 *
 * @see DistillerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoozhoundApp.class)
public class DistillerResourceIntTest {
    private static final String DEFAULT_DISTILLER_NAME = "AAAAA";
    private static final String UPDATED_DISTILLER_NAME = "BBBBB";

    private static final Integer DEFAULT_DISTILLER_RATING = 1;
    private static final Integer UPDATED_DISTILLER_RATING = 2;

    @Inject
    private DistillerRepository distillerRepository;

    @Inject
    private DistillerService distillerService;

    @Inject
    private DistillerSearchRepository distillerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDistillerMockMvc;

    private Distiller distiller;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DistillerResource distillerResource = new DistillerResource();
        ReflectionTestUtils.setField(distillerResource, "distillerService", distillerService);
        this.restDistillerMockMvc = MockMvcBuilders.standaloneSetup(distillerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distiller createEntity(EntityManager em) {
        Distiller distiller = new Distiller();
        distiller = new Distiller()
                .distillerName(DEFAULT_DISTILLER_NAME)
                .distillerRating(DEFAULT_DISTILLER_RATING);
        return distiller;
    }

    @Before
    public void initTest() {
        distillerSearchRepository.deleteAll();
        distiller = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistiller() throws Exception {
        int databaseSizeBeforeCreate = distillerRepository.findAll().size();

        // Create the Distiller

        restDistillerMockMvc.perform(post("/api/distillers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(distiller)))
                .andExpect(status().isCreated());

        // Validate the Distiller in the database
        List<Distiller> distillers = distillerRepository.findAll();
        assertThat(distillers).hasSize(databaseSizeBeforeCreate + 1);
        Distiller testDistiller = distillers.get(distillers.size() - 1);
        assertThat(testDistiller.getDistillerName()).isEqualTo(DEFAULT_DISTILLER_NAME);
        assertThat(testDistiller.getDistillerRating()).isEqualTo(DEFAULT_DISTILLER_RATING);

        // Validate the Distiller in ElasticSearch
        Distiller distillerEs = distillerSearchRepository.findOne(testDistiller.getId());
        assertThat(distillerEs).isEqualToComparingFieldByField(testDistiller);
    }

    @Test
    @Transactional
    public void checkDistillerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = distillerRepository.findAll().size();
        // set the field null
        distiller.setDistillerName(null);

        // Create the Distiller, which fails.

        restDistillerMockMvc.perform(post("/api/distillers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(distiller)))
                .andExpect(status().isBadRequest());

        List<Distiller> distillers = distillerRepository.findAll();
        assertThat(distillers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDistillers() throws Exception {
        // Initialize the database
        distillerRepository.saveAndFlush(distiller);

        // Get all the distillers
        restDistillerMockMvc.perform(get("/api/distillers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(distiller.getId().intValue())))
                .andExpect(jsonPath("$.[*].distillerName").value(hasItem(DEFAULT_DISTILLER_NAME.toString())))
                .andExpect(jsonPath("$.[*].distillerRating").value(hasItem(DEFAULT_DISTILLER_RATING)));
    }

    @Test
    @Transactional
    public void getDistiller() throws Exception {
        // Initialize the database
        distillerRepository.saveAndFlush(distiller);

        // Get the distiller
        restDistillerMockMvc.perform(get("/api/distillers/{id}", distiller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distiller.getId().intValue()))
            .andExpect(jsonPath("$.distillerName").value(DEFAULT_DISTILLER_NAME.toString()))
            .andExpect(jsonPath("$.distillerRating").value(DEFAULT_DISTILLER_RATING));
    }

    @Test
    @Transactional
    public void getNonExistingDistiller() throws Exception {
        // Get the distiller
        restDistillerMockMvc.perform(get("/api/distillers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistiller() throws Exception {
        // Initialize the database
        distillerService.save(distiller);

        int databaseSizeBeforeUpdate = distillerRepository.findAll().size();

        // Update the distiller
        Distiller updatedDistiller = distillerRepository.findOne(distiller.getId());
        updatedDistiller
                .distillerName(UPDATED_DISTILLER_NAME)
                .distillerRating(UPDATED_DISTILLER_RATING);

        restDistillerMockMvc.perform(put("/api/distillers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDistiller)))
                .andExpect(status().isOk());

        // Validate the Distiller in the database
        List<Distiller> distillers = distillerRepository.findAll();
        assertThat(distillers).hasSize(databaseSizeBeforeUpdate);
        Distiller testDistiller = distillers.get(distillers.size() - 1);
        assertThat(testDistiller.getDistillerName()).isEqualTo(UPDATED_DISTILLER_NAME);
        assertThat(testDistiller.getDistillerRating()).isEqualTo(UPDATED_DISTILLER_RATING);

        // Validate the Distiller in ElasticSearch
        Distiller distillerEs = distillerSearchRepository.findOne(testDistiller.getId());
        assertThat(distillerEs).isEqualToComparingFieldByField(testDistiller);
    }

    @Test
    @Transactional
    public void deleteDistiller() throws Exception {
        // Initialize the database
        distillerService.save(distiller);

        int databaseSizeBeforeDelete = distillerRepository.findAll().size();

        // Get the distiller
        restDistillerMockMvc.perform(delete("/api/distillers/{id}", distiller.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean distillerExistsInEs = distillerSearchRepository.exists(distiller.getId());
        assertThat(distillerExistsInEs).isFalse();

        // Validate the database is empty
        List<Distiller> distillers = distillerRepository.findAll();
        assertThat(distillers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDistiller() throws Exception {
        // Initialize the database
        distillerService.save(distiller);

        // Search the distiller
        restDistillerMockMvc.perform(get("/api/_search/distillers?query=id:" + distiller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distiller.getId().intValue())))
            .andExpect(jsonPath("$.[*].distillerName").value(hasItem(DEFAULT_DISTILLER_NAME.toString())))
            .andExpect(jsonPath("$.[*].distillerRating").value(hasItem(DEFAULT_DISTILLER_RATING)));
    }
}
