import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('MpkBudgetMapper e2e test', () => {

    let navBarPage: NavBarPage;
    let mpkBudgetMapperDialogPage: MpkBudgetMapperDialogPage;
    let mpkBudgetMapperComponentsPage: MpkBudgetMapperComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load MpkBudgetMappers', () => {
        navBarPage.goToEntity('mpk-budget-mapper');
        mpkBudgetMapperComponentsPage = new MpkBudgetMapperComponentsPage();
        expect(mpkBudgetMapperComponentsPage.getTitle()).toMatch(/tnApp.mpkBudgetMapper.home.title/);

    });

    it('should load create MpkBudgetMapper dialog', () => {
        mpkBudgetMapperComponentsPage.clickOnCreateButton();
        mpkBudgetMapperDialogPage = new MpkBudgetMapperDialogPage();
        expect(mpkBudgetMapperDialogPage.getModalTitle()).toMatch(/tnApp.mpkBudgetMapper.home.createOrEditLabel/);
        mpkBudgetMapperDialogPage.close();
    });

   /* it('should create and save MpkBudgetMappers', () => {
        mpkBudgetMapperComponentsPage.clickOnCreateButton();
        mpkBudgetMapperDialogPage.setMpkInput('mpk');
        expect(mpkBudgetMapperDialogPage.getMpkInput()).toMatch('mpk');
        mpkBudgetMapperDialogPage.setBudgetInput('budget');
        expect(mpkBudgetMapperDialogPage.getBudgetInput()).toMatch('budget');
        mpkBudgetMapperDialogPage.setDescriptionInput('description');
        expect(mpkBudgetMapperDialogPage.getDescriptionInput()).toMatch('description');
        mpkBudgetMapperDialogPage.clientSelectLastOption();
        mpkBudgetMapperDialogPage.save();
        expect(mpkBudgetMapperDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class MpkBudgetMapperComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-mpk-budget-mapper div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MpkBudgetMapperDialogPage {
    modalTitle = element(by.css('h4#myMpkBudgetMapperLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    mpkInput = element(by.css('input#field_mpk'));
    budgetInput = element(by.css('input#field_budget'));
    descriptionInput = element(by.css('input#field_description'));
    clientSelect = element(by.css('select#field_client'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setMpkInput = function (mpk) {
        this.mpkInput.sendKeys(mpk);
    }

    getMpkInput = function () {
        return this.mpkInput.getAttribute('value');
    }

    setBudgetInput = function (budget) {
        this.budgetInput.sendKeys(budget);
    }

    getBudgetInput = function () {
        return this.budgetInput.getAttribute('value');
    }

    setDescriptionInput = function (description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function () {
        return this.descriptionInput.getAttribute('value');
    }

    clientSelectLastOption = function () {
        this.clientSelect.all(by.tagName('option')).last().click();
    }

    clientSelectOption = function (option) {
        this.clientSelect.sendKeys(option);
    }

    getClientSelect = function () {
        return this.clientSelect;
    }

    getClientSelectedOption = function () {
        return this.clientSelect.element(by.css('option:checked')).getText();
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
