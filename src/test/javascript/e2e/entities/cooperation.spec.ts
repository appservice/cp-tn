import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Cooperation e2e test', () => {

    let navBarPage: NavBarPage;
    let cooperationDialogPage: CooperationDialogPage;
    let cooperationComponentsPage: CooperationComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Cooperation', () => {
        navBarPage.goToEntity('cooperation');
        cooperationComponentsPage = new CooperationComponentsPage();
        expect(cooperationComponentsPage.getTitle())
            .toMatch(/tnApp.cooperation.home.title/);

    });

    it('should load create Cooperation dialog', () => {
        cooperationComponentsPage.clickOnCreateButton();
        cooperationDialogPage = new CooperationDialogPage();
        expect(cooperationDialogPage.getModalTitle())
            .toMatch(/tnApp.cooperation.home.createOrEditLabel/);
        cooperationDialogPage.close();
    });

    it('should create and save Cooperation', () => {
        cooperationComponentsPage.clickOnCreateButton();
        cooperationDialogPage.setNameInput('name');
        expect(cooperationDialogPage.getNameInput()).toMatch('name');
        cooperationDialogPage.setCounterpartyInput('counterparty');
        expect(cooperationDialogPage.getCounterpartyInput()).toMatch('counterparty');
        cooperationDialogPage.setAmountInput('5');
        expect(cooperationDialogPage.getAmountInput()).toMatch('5');
        cooperationDialogPage.setPriceInput('5');
        expect(cooperationDialogPage.getPriceInput()).toMatch('5');
        cooperationDialogPage.estimationSelectLastOption();
        cooperationDialogPage.save();
        expect(cooperationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CooperationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cooperation div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CooperationDialogPage {
    modalTitle = element(by.css('h4#myCooperationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    counterpartyInput = element(by.css('input#field_counterparty'));
    amountInput = element(by.css('input#field_amount'));
    priceInput = element(by.css('input#field_price'));
    estimationSelect = element(by.css('select#field_estimation'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    }

    setCounterpartyInput = function(counterparty) {
        this.counterpartyInput.sendKeys(counterparty);
    }

    getCounterpartyInput = function() {
        return this.counterpartyInput.getAttribute('value');
    }

    setAmountInput = function(amount) {
        this.amountInput.sendKeys(amount);
    }

    getAmountInput = function() {
        return this.amountInput.getAttribute('value');
    }

    setPriceInput = function(price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function() {
        return this.priceInput.getAttribute('value');
    }

    estimationSelectLastOption = function() {
        this.estimationSelect.all(by.tagName('option')).last().click();
    }

    estimationSelectOption = function(option) {
        this.estimationSelect.sendKeys(option);
    }

    getEstimationSelect = function() {
        return this.estimationSelect;
    }

    getEstimationSelectedOption = function() {
        return this.estimationSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
