package vn.vnpay.demo1.service;

import vn.vnpay.demo1.domain.BankRequest;
import vn.vnpay.demo1.exception.BankNotFoundException;
import vn.vnpay.demo1.exception.ClassAlreadyExistException;
import vn.vnpay.demo1.exception.DataNotCorrectException;
import vn.vnpay.demo1.domain.BankResponse;

public interface BankService {
    BankResponse addDataToRedis(BankRequest bankRequest) throws ClassAlreadyExistException,
    BankNotFoundException, DataNotCorrectException;

}
