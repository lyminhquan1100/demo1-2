package vn.vnpay.demo1.service;

import com.google.gson.Gson;
import org.slf4j.MDC;
import vn.vnpay.demo1.config.BankConfiguration;
import vn.vnpay.demo1.config.Message;
import vn.vnpay.demo1.config.RedisConfig;
import vn.vnpay.demo1.domain.Bank;
import vn.vnpay.demo1.domain.BankRequest;
import vn.vnpay.demo1.exception.BankNotFoundException;
import vn.vnpay.demo1.exception.DataNotCorrectException;
import vn.vnpay.demo1.domain.BankResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.UUID;

// vn.vnpay, them interface
@Service
@Slf4j
public class BankServiceImpl implements BankService {
    private static final String TOKEN = "token";
    private final BankConfiguration bankConfiguration;

    private final RedisConfig redisConfig;
    private final RedisTemplate<String, Object> template;

    public BankServiceImpl(BankConfiguration bankConfiguration, RedisConfig redisConfig, RedisTemplate<String, Object> template) {
        this.bankConfiguration = bankConfiguration;
        this.redisConfig = redisConfig;
        this.template = template;
    }

    @Override
    public BankResponse addDataToRedis(BankRequest bankRequest) // save?
    {
        String token = UUID.randomUUID().toString();
        MDC.put(TOKEN, token);
        log.info("Begin addDataToRedis : {}", bankRequest);// Them log begin
        Bank bank = bankConfiguration.findByBankCode(bankRequest.getBankCode()); //bankname?
        log.info("Find bank code : nameBank = {}", bankRequest.getBankCode());
        if (ObjectUtils.isEmpty(bank)) {
            log.info("Bank not found"); // error ->info
            throw new BankNotFoundException(Message.BANK_NOT_FOUND);
        }
        String checkSum = getCheckSum(bankRequest, bank.getPrivateKey());
        String checkSumSha256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(checkSum);
        String code;
        String message;
        UUID uuid = UUID.randomUUID();
        if (!Objects.equals(bankRequest.getCheckSum(), checkSumSha256)) { //return, throw first
            log.info("Data not correct");
            throw new DataNotCorrectException(Message.DATA_NOT_CORRECT);
        }
        code = Message.CODE_SUCCESS;
        message = Message.SUCCESS;
        Gson gson = new Gson();
        String bankRequestDTOJson = gson.toJson(bankRequest);
//        template.opsForHash().put(bankRequest.getBankCode(), bankRequest.getTokenKey(), bankRequestDTOJson);
        redisConfig.save(bankRequest);
        log.info("BankService up to Redis : hash = {}, key = {}", bankRequest.getBankCode(),
                bankRequest.getTokenKey());
        return getBankResponse(code, message, uuid, bank.getPrivateKey()); // bo VM
    }

    private BankResponse getBankResponse(String code, String message, UUID uuid, String privateKey) { // Bo static
        log.info("Begin getBankResponse");
        BankResponse bankResponse = BankResponse.builder()
                .code(code)
                .message(message)
                .responseId(uuid.toString())
                .responseTime(System.currentTimeMillis())
                .build();
        String checkSumResponse = getCheckSumResponse(bankResponse, privateKey); // check?
        String checkSumResponseSha256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(checkSumResponse); // check?
        bankResponse.setCheckSum(checkSumResponseSha256);
        log.info("Response status Bank: code = {}, message = {}", code, message);
        return bankResponse;
    }

    private String getCheckSum(BankRequest bankRequest, String privateKey) { // xoa static
        log.info("Begin getCheckSum");
        //  bankRequestDTO = null;  ghi log, khong throw exception
        StringBuilder checkSum = new StringBuilder();
        checkSum.append(bankRequest.getMobile()).append(bankRequest.getBankCode())
                .append(bankRequest.getAccountNo())
                .append(bankRequest.getPayDate()).append(bankRequest.getDebitAmount())
                .append(bankRequest.getRespCode())
                .append(bankRequest.getTraceTransfer()).append(bankRequest.getMessageType()).append(privateKey);
        log.info("Return checkSum {}", checkSum);
        return checkSum.toString();
    }

    private String getCheckSumResponse(BankResponse bankResponse, String privateKey) { // check?
        log.info("Begin getCheckSumResponse");
        StringBuilder checkSumResponse = new StringBuilder();
        checkSumResponse.append(bankResponse.getCode()).append(bankResponse.getMessage())
                .append(bankResponse.getResponseId())
                .append(bankResponse.getResponseTime()).append(privateKey);
        log.info("Return checkSumResponse {}", checkSumResponse);
        return checkSumResponse.toString();
    }

}
