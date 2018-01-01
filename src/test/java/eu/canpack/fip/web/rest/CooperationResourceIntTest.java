package eu.canpack.fip.web.rest;

import eu.canpack.fip.TnApp;
import eu.canpack.fip.bo.cooperation.*;
import eu.canpack.fip.bo.cooperation.dto.CooperationDTO;
import eu.canpack.fip.bo.cooperation.dto.CooperationMapper;
import eu.canpack.fip.repository.search.CooperationSearchRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static eu.canpack.fip.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CooperationResource REST controller.
 *
 * @see CooperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnApp.class)
public class CooperationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTERPARTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    @Autowired
    private CooperationRepository cooperationRepository;

    @Autowired
    private CooperationMapper cooperationMapper;

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private CooperationSearchRepository cooperationSearchRepository;

    @Autowired
    private CooperationQueryService cooperationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCooperationMockMvc;

    private Cooperation cooperation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CooperationResource cooperationResource = new CooperationResource(cooperationService, cooperationQueryService);
        this.restCooperationMockMvc = MockMvcBuilders.standaloneSetup(cooperationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperation createEntity(EntityManager em) {
        Cooperation cooperation = new Cooperation()
            .name(DEFAULT_NAME)
            .counterparty(DEFAULT_COUNTERPARTY)
            .amount(DEFAULT_AMOUNT)
            .price(DEFAULT_PRICE);
        return cooperation;
    }

    @Before
    public void initTest() {
        cooperationSearchRepository.deleteAll();
        cooperation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCooperation() throws Exception {
        int databaseSizeBeforeCreate = cooperationRepository.findAll().size();

        // Create the Cooperation
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);
        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isCreated());

        // Validate the Cooperation in the database
        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeCreate + 1);
        Cooperation testCooperation = cooperationList.get(cooperationList.size() - 1);
        assertThat(testCooperation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCooperation.getCounterparty()).isEqualTo(DEFAULT_COUNTERPARTY);
        assertThat(testCooperation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCooperation.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Cooperation in Elasticsearch
        Cooperation cooperationEs = cooperationSearchRepository.findOne(testCooperation.getId());
        assertThat(cooperationEs).isEqualToIgnoringGivenFields(testCooperation);
    }

    @Test
    @Transactional
    public void createCooperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cooperationRepository.findAll().size();

        // Create the Cooperation with an existing ID
        cooperation.setId(1L);
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cooperation in the database
        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperationRepository.findAll().size();
        // set the field null
        cooperation.setName(null);

        // Create the Cooperation, which fails.
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isBadRequest());

        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCounterpartyIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperationRepository.findAll().size();
        // set the field null
        cooperation.setCounterparty(null);

        // Create the Cooperation, which fails.
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isBadRequest());

        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperationRepository.findAll().size();
        // set the field null
        cooperation.setAmount(null);

        // Create the Cooperation, which fails.
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isBadRequest());

        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperationRepository.findAll().size();
        // set the field null
        cooperation.setPrice(null);

        // Create the Cooperation, which fails.
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        restCooperationMockMvc.perform(post("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isBadRequest());

        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCooperation() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList
        restCooperationMockMvc.perform(get("/api/cooperation?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].counterparty").value(hasItem(DEFAULT_COUNTERPARTY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getCooperation() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get the cooperation
        restCooperationMockMvc.perform(get("/api/cooperation/{id}", cooperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cooperation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.counterparty").value(DEFAULT_COUNTERPARTY.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllCooperationByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where name equals to DEFAULT_NAME
        defaultCooperationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cooperationList where name equals to UPDATED_NAME
        defaultCooperationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCooperationByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCooperationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cooperationList where name equals to UPDATED_NAME
        defaultCooperationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCooperationByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where name is not null
        defaultCooperationShouldBeFound("name.specified=true");

        // Get all the cooperationList where name is null
        defaultCooperationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCooperationByCounterpartyIsEqualToSomething() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where counterparty equals to DEFAULT_COUNTERPARTY
        defaultCooperationShouldBeFound("counterparty.equals=" + DEFAULT_COUNTERPARTY);

        // Get all the cooperationList where counterparty equals to UPDATED_COUNTERPARTY
        defaultCooperationShouldNotBeFound("counterparty.equals=" + UPDATED_COUNTERPARTY);
    }

    @Test
    @Transactional
    public void getAllCooperationByCounterpartyIsInShouldWork() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where counterparty in DEFAULT_COUNTERPARTY or UPDATED_COUNTERPARTY
        defaultCooperationShouldBeFound("counterparty.in=" + DEFAULT_COUNTERPARTY + "," + UPDATED_COUNTERPARTY);

        // Get all the cooperationList where counterparty equals to UPDATED_COUNTERPARTY
        defaultCooperationShouldNotBeFound("counterparty.in=" + UPDATED_COUNTERPARTY);
    }

    @Test
    @Transactional
    public void getAllCooperationByCounterpartyIsNullOrNotNull() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where counterparty is not null
        defaultCooperationShouldBeFound("counterparty.specified=true");

        // Get all the cooperationList where counterparty is null
        defaultCooperationShouldNotBeFound("counterparty.specified=false");
    }

    @Test
    @Transactional
    public void getAllCooperationByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where amount equals to DEFAULT_AMOUNT
        defaultCooperationShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the cooperationList where amount equals to UPDATED_AMOUNT
        defaultCooperationShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCooperationByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultCooperationShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the cooperationList where amount equals to UPDATED_AMOUNT
        defaultCooperationShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCooperationByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where amount is not null
        defaultCooperationShouldBeFound("amount.specified=true");

        // Get all the cooperationList where amount is null
        defaultCooperationShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCooperationByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where price equals to DEFAULT_PRICE
        defaultCooperationShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the cooperationList where price equals to UPDATED_PRICE
        defaultCooperationShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCooperationByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCooperationShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the cooperationList where price equals to UPDATED_PRICE
        defaultCooperationShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCooperationByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);

        // Get all the cooperationList where price is not null
        defaultCooperationShouldBeFound("price.specified=true");

        // Get all the cooperationList where price is null
        defaultCooperationShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllCooperationByEstimationIsEqualToSomething() throws Exception {
        // Initialize the database
//        Estimation estimation = EstimationResourceIntTest.createEntity(em);
//        em.persist(estimation);
        em.flush();
//        cooperation.setEstimation(estimation);
//        cooperationRepository.saveAndFlush(cooperation);
//        Long estimationId = estimation.getId();

//        // Get all the cooperationList where estimation equals to estimationId
//        defaultCooperationShouldBeFound("estimationId.equals=" + estimationId);
//
//        // Get all the cooperationList where estimation equals to estimationId + 1
//        defaultCooperationShouldNotBeFound("estimationId.equals=" + (estimationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCooperationShouldBeFound(String filter) throws Exception {
        restCooperationMockMvc.perform(get("/api/cooperation?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].counterparty").value(hasItem(DEFAULT_COUNTERPARTY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCooperationShouldNotBeFound(String filter) throws Exception {
        restCooperationMockMvc.perform(get("/api/cooperation?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCooperation() throws Exception {
        // Get the cooperation
        restCooperationMockMvc.perform(get("/api/cooperation/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCooperation() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);
        cooperationSearchRepository.save(cooperation);
        int databaseSizeBeforeUpdate = cooperationRepository.findAll().size();

        // Update the cooperation
        Cooperation updatedCooperation = cooperationRepository.findOne(cooperation.getId());
        // Disconnect from session so that the updates on updatedCooperation are not directly saved in db
        em.detach(updatedCooperation);
        updatedCooperation
            .name(UPDATED_NAME)
            .counterparty(UPDATED_COUNTERPARTY)
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE);
        CooperationDTO cooperationDTO = cooperationMapper.toDto(updatedCooperation);

        restCooperationMockMvc.perform(put("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isOk());

        // Validate the Cooperation in the database
        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeUpdate);
        Cooperation testCooperation = cooperationList.get(cooperationList.size() - 1);
        assertThat(testCooperation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCooperation.getCounterparty()).isEqualTo(UPDATED_COUNTERPARTY);
        assertThat(testCooperation.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCooperation.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Cooperation in Elasticsearch
        Cooperation cooperationEs = cooperationSearchRepository.findOne(testCooperation.getId());
        assertThat(cooperationEs).isEqualToIgnoringGivenFields(testCooperation);
    }

    @Test
    @Transactional
    public void updateNonExistingCooperation() throws Exception {
        int databaseSizeBeforeUpdate = cooperationRepository.findAll().size();

        // Create the Cooperation
        CooperationDTO cooperationDTO = cooperationMapper.toDto(cooperation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCooperationMockMvc.perform(put("/api/cooperation")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cooperationDTO)))
            .andExpect(status().isCreated());

        // Validate the Cooperation in the database
        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCooperation() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);
        cooperationSearchRepository.save(cooperation);
        int databaseSizeBeforeDelete = cooperationRepository.findAll().size();

        // Get the cooperation
        restCooperationMockMvc.perform(delete("/api/cooperation/{id}", cooperation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cooperationExistsInEs = cooperationSearchRepository.exists(cooperation.getId());
        assertThat(cooperationExistsInEs).isFalse();

        // Validate the database is empty
        List<Cooperation> cooperationList = cooperationRepository.findAll();
        assertThat(cooperationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCooperation() throws Exception {
        // Initialize the database
        cooperationRepository.saveAndFlush(cooperation);
        cooperationSearchRepository.save(cooperation);

        // Search the cooperation
        restCooperationMockMvc.perform(get("/api/_search/cooperation?query=id:" + cooperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].counterparty").value(hasItem(DEFAULT_COUNTERPARTY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cooperation.class);
        Cooperation cooperation1 = new Cooperation();
        cooperation1.setId(1L);
        Cooperation cooperation2 = new Cooperation();
        cooperation2.setId(cooperation1.getId());
        assertThat(cooperation1).isEqualTo(cooperation2);
        cooperation2.setId(2L);
        assertThat(cooperation1).isNotEqualTo(cooperation2);
        cooperation1.setId(null);
        assertThat(cooperation1).isNotEqualTo(cooperation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperationDTO.class);
        CooperationDTO cooperationDTO1 = new CooperationDTO();
        cooperationDTO1.setId(1L);
        CooperationDTO cooperationDTO2 = new CooperationDTO();
        assertThat(cooperationDTO1).isNotEqualTo(cooperationDTO2);
        cooperationDTO2.setId(cooperationDTO1.getId());
        assertThat(cooperationDTO1).isEqualTo(cooperationDTO2);
        cooperationDTO2.setId(2L);
        assertThat(cooperationDTO1).isNotEqualTo(cooperationDTO2);
        cooperationDTO1.setId(null);
        assertThat(cooperationDTO1).isNotEqualTo(cooperationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cooperationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cooperationMapper.fromId(null)).isNull();
    }
}
