package eu.canpack.fip.web.rest;

import eu.canpack.fip.TnApp;

import eu.canpack.fip.bo.operator.OperatorResource;
import eu.canpack.fip.bo.operator.Operator;
import eu.canpack.fip.bo.pdf.OperatorCardCreatorService;
import eu.canpack.fip.repository.OperatorRepository;
import eu.canpack.fip.bo.operator.OperatorService;
import eu.canpack.fip.repository.search.OperatorSearchRepository;
import eu.canpack.fip.bo.operator.OperatorDTO;
import eu.canpack.fip.service.mapper.OperatorMapper;
import eu.canpack.fip.web.rest.errors.ExceptionTranslator;
import eu.canpack.fip.bo.operator.OperatorQueryService;

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
 * Test class for the OperatorResource REST controller.
 *
 * @see OperatorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TnApp.class)
public class OperatorResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorSearchRepository operatorSearchRepository;

    @Autowired
    private OperatorQueryService operatorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private OperatorCardCreatorService operatorCardCreatorService;

    @Autowired
    private EntityManager em;

    private MockMvc restOperatorMockMvc;

    private Operator operator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OperatorResource operatorResource = new OperatorResource(operatorService, operatorQueryService, operatorCardCreatorService);
        this.restOperatorMockMvc = MockMvcBuilders.standaloneSetup(operatorResource)
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
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .jobTitle(DEFAULT_JOB_TITLE)
            .active(DEFAULT_ACTIVE);
        return operator;
    }

    @Before
    public void initTest() {
        operatorSearchRepository.deleteAll();
        operator = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperator() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);
        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate + 1);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOperator.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOperator.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testOperator.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testOperator.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Operator in Elasticsearch
        Operator operatorEs = operatorSearchRepository.findOne(testOperator.getId());
        assertThat(operatorEs).isEqualToComparingFieldByField(testOperator);
    }

    @Test
    @Transactional
    public void createOperatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operatorRepository.findAll().size();

        // Create the Operator with an existing ID
        operator.setId(1L);
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorRepository.findAll().size();
        // set the field null
        operator.setFirstName(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorRepository.findAll().size();
        // set the field null
        operator.setLastName(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorRepository.findAll().size();
        // set the field null
        operator.setCardNumber(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorRepository.findAll().size();
        // set the field null
        operator.setJobTitle(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc.perform(post("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc.perform(get("/api/operators/{id}", operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllOperatorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where firstName equals to DEFAULT_FIRST_NAME
        defaultOperatorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the operatorList where firstName equals to UPDATED_FIRST_NAME
        defaultOperatorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllOperatorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultOperatorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the operatorList where firstName equals to UPDATED_FIRST_NAME
        defaultOperatorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllOperatorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where firstName is not null
        defaultOperatorShouldBeFound("firstName.specified=true");

        // Get all the operatorList where firstName is null
        defaultOperatorShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperatorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where lastName equals to DEFAULT_LAST_NAME
        defaultOperatorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the operatorList where lastName equals to UPDATED_LAST_NAME
        defaultOperatorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllOperatorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultOperatorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the operatorList where lastName equals to UPDATED_LAST_NAME
        defaultOperatorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllOperatorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where lastName is not null
        defaultOperatorShouldBeFound("lastName.specified=true");

        // Get all the operatorList where lastName is null
        defaultOperatorShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperatorsByCardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where cardNumber equals to DEFAULT_CARD_NUMBER
        defaultOperatorShouldBeFound("cardNumber.equals=" + DEFAULT_CARD_NUMBER);

        // Get all the operatorList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultOperatorShouldNotBeFound("cardNumber.equals=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where cardNumber in DEFAULT_CARD_NUMBER or UPDATED_CARD_NUMBER
        defaultOperatorShouldBeFound("cardNumber.in=" + DEFAULT_CARD_NUMBER + "," + UPDATED_CARD_NUMBER);

        // Get all the operatorList where cardNumber equals to UPDATED_CARD_NUMBER
        defaultOperatorShouldNotBeFound("cardNumber.in=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOperatorsByCardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where cardNumber is not null
        defaultOperatorShouldBeFound("cardNumber.specified=true");

        // Get all the operatorList where cardNumber is null
        defaultOperatorShouldNotBeFound("cardNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperatorsByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultOperatorShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the operatorList where jobTitle equals to UPDATED_JOB_TITLE
        defaultOperatorShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    public void getAllOperatorsByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultOperatorShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the operatorList where jobTitle equals to UPDATED_JOB_TITLE
        defaultOperatorShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    public void getAllOperatorsByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where jobTitle is not null
        defaultOperatorShouldBeFound("jobTitle.specified=true");

        // Get all the operatorList where jobTitle is null
        defaultOperatorShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperatorsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where active equals to DEFAULT_ACTIVE
        defaultOperatorShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the operatorList where active equals to UPDATED_ACTIVE
        defaultOperatorShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOperatorsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultOperatorShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the operatorList where active equals to UPDATED_ACTIVE
        defaultOperatorShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllOperatorsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where active is not null
        defaultOperatorShouldBeFound("active.specified=true");

        // Get all the operatorList where active is null
        defaultOperatorShouldNotBeFound("active.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc.perform(get("/api/operators?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get("/api/operators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);
        operatorSearchRepository.save(operator);
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Update the operator
        Operator updatedOperator = operatorRepository.findOne(operator.getId());
        updatedOperator
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .cardNumber(UPDATED_CARD_NUMBER)
            .jobTitle(UPDATED_JOB_TITLE)
            .active(UPDATED_ACTIVE);
        OperatorDTO operatorDTO = operatorMapper.toDto(updatedOperator);

        restOperatorMockMvc.perform(put("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isOk());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate);
        Operator testOperator = operatorList.get(operatorList.size() - 1);
        assertThat(testOperator.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOperator.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOperator.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testOperator.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testOperator.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Operator in Elasticsearch
        Operator operatorEs = operatorSearchRepository.findOne(testOperator.getId());
        assertThat(operatorEs).isEqualToComparingFieldByField(testOperator);
    }

    @Test
    @Transactional
    public void updateNonExistingOperator() throws Exception {
        int databaseSizeBeforeUpdate = operatorRepository.findAll().size();

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOperatorMockMvc.perform(put("/api/operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Operator in the database
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);
        operatorSearchRepository.save(operator);
        int databaseSizeBeforeDelete = operatorRepository.findAll().size();

        // Get the operator
        restOperatorMockMvc.perform(delete("/api/operators/{id}", operator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean operatorExistsInEs = operatorSearchRepository.exists(operator.getId());
        assertThat(operatorExistsInEs).isFalse();

        // Validate the database is empty
        List<Operator> operatorList = operatorRepository.findAll();
        assertThat(operatorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);
        operatorSearchRepository.save(operator);

        // Search the operator
        restOperatorMockMvc.perform(get("/api/_search/operators?query=id:" + operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operator.class);
        Operator operator1 = new Operator();
        operator1.setId(1L);
        Operator operator2 = new Operator();
        operator2.setId(operator1.getId());
        assertThat(operator1).isEqualTo(operator2);
        operator2.setId(2L);
        assertThat(operator1).isNotEqualTo(operator2);
        operator1.setId(null);
        assertThat(operator1).isNotEqualTo(operator2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperatorDTO.class);
        OperatorDTO operatorDTO1 = new OperatorDTO();
        operatorDTO1.setId(1L);
        OperatorDTO operatorDTO2 = new OperatorDTO();
        assertThat(operatorDTO1).isNotEqualTo(operatorDTO2);
        operatorDTO2.setId(operatorDTO1.getId());
        assertThat(operatorDTO1).isEqualTo(operatorDTO2);
        operatorDTO2.setId(2L);
        assertThat(operatorDTO1).isNotEqualTo(operatorDTO2);
        operatorDTO1.setId(null);
        assertThat(operatorDTO1).isNotEqualTo(operatorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(operatorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(operatorMapper.fromId(null)).isNull();
    }
}
