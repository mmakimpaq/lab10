package savings.web.impl;

import static common.json.MoneyModule.moneyPropertyEditor;
import static org.joda.time.DateTime.now;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;
import savings.model.PaybackConfirmation;
import savings.model.Purchase;
import savings.service.PaybackBookKeeper;

import javax.management.relation.RoleUnresolved;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.ws.Response;

@Controller
@RequestMapping("/payback")
//TODO #3 handle expection RuntimeException, returning status code 418 (HttpStatus.I_AM_A_TEAPOT)
public class PaybackController {

    private final PaybackBookKeeper paybackBookKeeper;

    @Autowired
    public PaybackController(PaybackBookKeeper paybackBookKeeper) {
        this.paybackBookKeeper = paybackBookKeeper;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Money.class, moneyPropertyEditor);
    }

    @RequestMapping(method = GET)
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("payback/list");
        modelAndView.addObject("paybacks", paybackBookKeeper.getAllPaybacks());
        return modelAndView;
    }


    @RequestMapping(value ="/teapot", method = GET)
    public void teapot(){
        throw new RuntimeException();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handler(){
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    /**
     * Notice that by convention this method returns a 'purchaseForm' model attribute
     * and looks up a view by id 'payback/new' to render in response.
     */
    @RequestMapping(value = "/new", method = GET)
    public PurchaseForm purchase() {
        PurchaseForm purchaseForm = new PurchaseForm();
        purchaseForm.setCreditCardNumber("1234123412341234");
        purchaseForm.setMerchantNumber("1234567890");
        return purchaseForm;
    }

    /**
     * Analogous to the above, this method returns a 'paybackConfirmation' model attribute
     * and looks up a view by id 'payback/confirm' to render in response.
     */
    // TODO #1 add validation for 'purchaseForm' and bindingResults
    // TODO #2 return ModelAndView with appropriate view, depending on validation results
    @RequestMapping(value = "/confirm", method = POST)
    public ModelAndView paybackConfirmation(@Valid @ModelAttribute PurchaseForm purchaseForm, BindingResult result) {

    if(!result.hasErrors()){
        paybackBookKeeper.registerPaybackFor(new Purchase(
                purchaseForm.transactionValue,
                purchaseForm.creditCardNumber,
                purchaseForm.merchantNumber,
                now()));

        ModelAndView modelAndView = new ModelAndView("payback/confirm");
        modelAndView.addObject(purchaseForm);
        return modelAndView;
    }
        ModelAndView modelAndView = new ModelAndView("payback/new");
        modelAndView.addObject(purchaseForm);
        return modelAndView;

    }

    @Data
    public static class PurchaseForm {

        @NotBlank
        public String creditCardNumber;
        @NotBlank
        public String merchantNumber;
        @NotNull
        public Money transactionValue;

    }
}
