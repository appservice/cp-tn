package eu.canpack.fip.web.rest;

import eu.canpack.fip.TnApp;

import eu.canpack.fip.domain.MpkBudgetMapper;
import eu.canpack.fip.domain.Client;
import eu.canpack.fip.repository.MpkBudgetMapperRepository;
import eu.canpack.fip.service.MpkBudgetMapperService;
import eu.canpack.fip.repository.search.MpkBudgetMapperSearchRepository;
import eu.canpack.fip.service.dto.MpkBudgetMapperDTO;
import eu.canpack.fip.service.mapper.MpkBudgetMapperMapper;
import eu.canpack.fip.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MpkBudgetMapperResource REST controller.
 *
 * @see MpkBudgetMapperResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnApp.class)
public class MpkBudgetMapperResourceIntTest {

    private static final String DEFAULT_MPK = "AAAAAA";
    private static final String UPDATED_MPK = "BBBBBB";

    private static final String DEFAULT_BUDGET = "";
    private static final String UPDATED_BUDGET = "9";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MpkBudgetMapperRepository mpkBudgetMapperRepository;

    @Autowired
    private MpkBudgetMapperMapper mpkBudgetMapperMapper;

    @Autowired
    private MpkBudgetMapperService mpkBudgetMapperService;

    @Autowired
    private MpkBudgetMapperSearchRepository mpkBudgetMapperSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMpkBudgetMapperMockMvc;

    private MpkBudgetMapper mpkBudgetMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MpkBudgetMapperResource mpkBudgetMapperResource = new MpkBudgetMapperResource(mpkBudgetMapperService);
        this.restMpkBudgetMapperMockMvc = MockMvcBuilders.standaloneSetup(mpkBudgetMapperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MpkBudgetMapper createEntity(EntityManager em) {
        MpkBudgetMapper mpkBudgetMapper = new MpkBudgetMapper()
            .mpk(DEFAULT_MPK)
            .budget(DEFAULT_BUDGET)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        mpkBudgetMapper.setClient(client);
        return mpkBudgetMapper;
    }

    @Before
    public void initTest() {
        mpkBudgetMapperSearchRepository.deleteAll();
        mpkBudgetMapper = createEntity(em);
    }

    @Test
    @Transactional
    public void createMpkBudgetMapper() throws Exception {
        int databaseSizeBeforeCreate = mpkBudgetMapperRepository.findAll().size();

        // Create the MpkBudgetMapper
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);
        restMpkBudgetMapperMockMvc.perform(post("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isCreated());

        // Validate the MpkBudgetMapper in the database
        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeCreate + 1);
        MpkBudgetMapper testMpkBudgetMapper = mpkBudgetMapperList.get(mpkBudgetMapperList.size() - 1);
        assertThat(testMpkBudgetMapper.getMpk()).isEqualTo(DEFAULT_MPK);
        assertThat(testMpkBudgetMapper.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testMpkBudgetMapper.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MpkBudgetMapper in Elasticsearch
        MpkBudgetMapper mpkBudgetMapperEs = mpkBudgetMapperSearchRepository.findOne(testMpkBudgetMapper.getId());
        assertThat(mpkBudgetMapperEs).isEqualToComparingFieldByField(testMpkBudgetMapper);
    }

    @Test
    @Transactional
    public void createMpkBudgetMapperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mpkBudgetMapperRepository.findAll().size();

        // Create the MpkBudgetMapper with an existing ID
        mpkBudgetMapper.setId(1L);
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMpkBudgetMapperMockMvc.perform(post("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MpkBudgetMapper in the database
        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMpkIsRequired() throws Exception {
        int databaseSizeBeforeTest = mpkBudgetMapperRepository.findAll().size();
        // set the field null
        mpkBudgetMapper.setMpk(null);

        // Create the MpkBudgetMapper, which fails.
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);

        restMpkBudgetMapperMockMvc.perform(post("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isBadRequest());

        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = mpkBudgetMapperRepository.findAll().size();
        // set the field null
        mpkBudgetMapper.setBudget(null);

        // Create the MpkBudgetMapper, which fails.
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);

        restMpkBudgetMapperMockMvc.perform(post("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isBadRequest());

        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMpkBudgetMappers() throws Exception {
        // Initialize the database
        mpkBudgetMapperRepository.saveAndFlush(mpkBudgetMapper);

        // Get all the mpkBudgetMapperList
        restMpkBudgetMapperMockMvc.perform(get("/api/mpk-budget-mappers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mpkBudgetMapper.getId().intValue())))
            .andExpect(jsonPath("$.[*].mpk").value(hasItem(DEFAULT_MPK.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMpkBudgetMapper() throws Exception {
        // Initialize the database
        mpkBudgetMapperRepository.saveAndFlush(mpkBudgetMapper);

        // Get the mpkBudgetMapper
        restMpkBudgetMapperMockMvc.perform(get("/api/mpk-budget-mappers/{id}", mpkBudgetMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mpkBudgetMapper.getId().intValue()))
            .andExpect(jsonPath("$.mpk").value(DEFAULT_MPK.toString()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMpkBudgetMapper() throws Exception {
        // Get the mpkBudgetMapper
        restMpkBudgetMapperMockMvc.perform(get("/api/mpk-budget-mappers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMpkBudgetMapper() throws Exception {
        // Initialize the database
        mpkBudgetMapperRepository.saveAndFlush(mpkBudgetMapper);
        mpkBudgetMapperSearchRepository.save(mpkBudgetMapper);
        int databaseSizeBeforeUpdate = mpkBudgetMapperRepository.findAll().size();

        // Update the mpkBudgetMapper
        MpkBudgetMapper updatedMpkBudgetMapper = mpkBudgetMapperRepository.findOne(mpkBudgetMapper.getId());
        updatedMpkBudgetMapper
            .mpk(UPDATED_MPK)
            .budget(UPDATED_BUDGET)
            .description(UPDATED_DESCRIPTION);
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(updatedMpkBudgetMapper);

        restMpkBudgetMapperMockMvc.perform(put("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isOk());

        // Validate the MpkBudgetMapper in the database
        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeUpdate);
        MpkBudgetMapper testMpkBudgetMapper = mpkBudgetMapperList.get(mpkBudgetMapperList.size() - 1);
        assertThat(testMpkBudgetMapper.getMpk()).isEqualTo(UPDATED_MPK);
        assertThat(testMpkBudgetMapper.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testMpkBudgetMapper.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MpkBudgetMapper in Elasticsearch
        MpkBudgetMapper mpkBudgetMapperEs = mpkBudgetMapperSearchRepository.findOne(testMpkBudgetMapper.getId());
        assertThat(mpkBudgetMapperEs).isEqualToComparingFieldByField(testMpkBudgetMapper);
    }

    @Test
    @Transactional
    public void updateNonExistingMpkBudgetMapper() throws Exception {
        int databaseSizeBeforeUpdate = mpkBudgetMapperRepository.findAll().size();

        // Create the MpkBudgetMapper
        MpkBudgetMapperDTO mpkBudgetMapperDTO = mpkBudgetMapperMapper.toDto(mpkBudgetMapper);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMpkBudgetMapperMockMvc.perform(put("/api/mpk-budget-mappers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mpkBudgetMapperDTO)))
            .andExpect(status().isCreated());

        // Validate the MpkBudgetMapper in the database
        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMpkBudgetMapper() throws Exception {
        // Initialize the database
        mpkBudgetMapperRepository.saveAndFlush(mpkBudgetMapper);
        mpkBudgetMapperSearchRepository.save(mpkBudgetMapper);
        int databaseSizeBeforeDelete = mpkBudgetMapperRepository.findAll().size();

        // Get the mpkBudgetMapper
        restMpkBudgetMapperMockMvc.perform(delete("/api/mpk-budget-mappers/{id}", mpkBudgetMapper.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mpkBudgetMapperExistsInEs = mpkBudgetMapperSearchRepository.exists(mpkBudgetMapper.getId());
        assertThat(mpkBudgetMapperExistsInEs).isFalse();

        // Validate the database is empty
        List<MpkBudgetMapper> mpkBudgetMapperList = mpkBudgetMapperRepository.findAll();
        assertThat(mpkBudgetMapperList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMpkBudgetMapper() throws Exception {
        // Initialize the database
        mpkBudgetMapperRepository.saveAndFlush(mpkBudgetMapper);
        mpkBudgetMapperSearchRepository.save(mpkBudgetMapper);

        // Search the mpkBudgetMapper
        restMpkBudgetMapperMockMvc.perform(get("/api/_search/mpk-budget-mappers?query=id:" + mpkBudgetMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mpkBudgetMapper.getId().intValue())))
            .andExpect(jsonPath("$.[*].mpk").value(hasItem(DEFAULT_MPK.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MpkBudgetMapper.class);
        MpkBudgetMapper mpkBudgetMapper1 = new MpkBudgetMapper();
        mpkBudgetMapper1.setId(1L);
        MpkBudgetMapper mpkBudgetMapper2 = new MpkBudgetMapper();
        mpkBudgetMapper2.setId(mpkBudgetMapper1.getId());
        assertThat(mpkBudgetMapper1).isEqualTo(mpkBudgetMapper2);
        mpkBudgetMapper2.setId(2L);
        assertThat(mpkBudgetMapper1).isNotEqualTo(mpkBudgetMapper2);
        mpkBudgetMapper1.setId(null);
        assertThat(mpkBudgetMapper1).isNotEqualTo(mpkBudgetMapper2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MpkBudgetMapperDTO.class);
        MpkBudgetMapperDTO mpkBudgetMapperDTO1 = new MpkBudgetMapperDTO();
        mpkBudgetMapperDTO1.setId(1L);
        MpkBudgetMapperDTO mpkBudgetMapperDTO2 = new MpkBudgetMapperDTO();
        assertThat(mpkBudgetMapperDTO1).isNotEqualTo(mpkBudgetMapperDTO2);
        mpkBudgetMapperDTO2.setId(mpkBudgetMapperDTO1.getId());
        assertThat(mpkBudgetMapperDTO1).isEqualTo(mpkBudgetMapperDTO2);
        mpkBudgetMapperDTO2.setId(2L);
        assertThat(mpkBudgetMapperDTO1).isNotEqualTo(mpkBudgetMapperDTO2);
        mpkBudgetMapperDTO1.setId(null);
        assertThat(mpkBudgetMapperDTO1).isNotEqualTo(mpkBudgetMapperDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mpkBudgetMapperMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mpkBudgetMapperMapper.fromId(null)).isNull();
    }
}
