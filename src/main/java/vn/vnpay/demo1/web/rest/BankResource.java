package vn.vnpay.demo1.web.rest;

import vn.vnpay.demo1.domain.BankRequest;
import vn.vnpay.demo1.exception.BankNotFoundException;
import vn.vnpay.demo1.exception.ClassAlreadyExistException;
import vn.vnpay.demo1.exception.DataNotCorrectException;
import vn.vnpay.demo1.service.BankService;
import vn.vnpay.demo1.service.BankServiceImpl;
import vn.vnpay.demo1.domain.BankResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/test")
public class BankResource {

    private final BankService bankService;
    private static final Logger log = LoggerFactory.getLogger(BankResource.class);

    public BankResource(BankServiceImpl bankService) {
        this.bankService = bankService;
    }

    @PostMapping()
    public ResponseEntity<BankResponse> bankResponseEntity(@RequestBody @Valid BankRequest dto)
            throws BankNotFoundException,
            DataNotCorrectException, ClassAlreadyExistException { // check object null?
        return ResponseEntity.ok(bankService.addDataToRedis(dto));
    }

}
