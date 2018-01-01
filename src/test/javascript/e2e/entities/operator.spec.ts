import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Operator e2e test', () => {

    let navBarPage: NavBarPage;
    let operatorDialogPage: OperatorDialogPage;
    let operatorComponentsPage: OperatorComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Operators', () => {
        navBarPage.goToEntity('operator');
        operatorComponentsPage = new OperatorComponentsPage();
        expect(operatorComponentsPage.getTitle())
            .toMatch(/tnApp.operator.home.title/);

    });

    it('should load create Operator dialog', () => {
        operatorComponentsPage.clickOnCreateButton();
        operatorDialogPage = new OperatorDialogPage();
        expect(operatorDialogPage.getModalTitle())
            .toMatch(/tnApp.operator.home.createOrEditLabel/);
        operatorDialogPage.close();
    });

    it('should create and save Operators', () => {
        operatorComponentsPage.clickOnCreateButton();
        operatorDialogPage.setFirstNameInput('firstName');
        expect(operatorDialogPage.getFirstNameInput()).toMatch('firstName');
        operatorDialogPage.setLastNameInput('lastName');
        expect(operatorDialogPage.getLastNameInput()).toMatch('lastName');
        operatorDialogPage.setCardNumberInput('cardNumber');
        expect(operatorDialogPage.getCardNumberInput()).toMatch('cardNumber');
        operatorDialogPage.setJobTitleInput('jobTitle');
        expect(operatorDialogPage.getJobTitleInput()).toMatch('jobTitle');
        operatorDialogPage.getActiveInput().isSelected().then((selected) => {
            if (selected) {
                operatorDialogPage.getActiveInput().click();
                expect(operatorDialogPage.getActiveInput().isSelected()).toBeFalsy();
            } else {
                operatorDialogPage.getActiveInput().click();
                expect(operatorDialogPage.getActiveInput().isSelected()).toBeTruthy();
            }
        });
        operatorDialogPage.save();
        expect(operatorDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class OperatorComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-operator div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class OperatorDialogPage {
    modalTitle = element(by.css('h4#myOperatorLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    firstNameInput = element(by.css('input#field_firstName'));
    lastNameInput = element(by.css('input#field_lastName'));
    cardNumberInput = element(by.css('input#field_cardNumber'));
    jobTitleInput = element(by.css('input#field_jobTitle'));
    activeInput = element(by.css('input#field_active'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFirstNameInput = function(firstName) {
        this.firstNameInput.sendKeys(firstName);
    }

    getFirstNameInput = function() {
        return this.firstNameInput.getAttribute('value');
    }

    setLastNameInput = function(lastName) {
        this.lastNameInput.sendKeys(lastName);
    }

    getLastNameInput = function() {
        return this.lastNameInput.getAttribute('value');
    }

    setCardNumberInput = function(cardNumber) {
        this.cardNumberInput.sendKeys(cardNumber);
    }

    getCardNumberInput = function() {
        return this.cardNumberInput.getAttribute('value');
    }

    setJobTitleInput = function(jobTitle) {
        this.jobTitleInput.sendKeys(jobTitle);
    }

    getJobTitleInput = function() {
        return this.jobTitleInput.getAttribute('value');
    }

    getActiveInput = function() {
        return this.activeInput;
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
