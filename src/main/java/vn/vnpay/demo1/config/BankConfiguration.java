package vn.vnpay.demo1.config;

import vn.vnpay.demo1.domain.Bank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "demo1")
public class BankConfiguration {
    private List<Bank> banks;
    public Bank findByBankCode(String bankCode) {
       /** Đặt tên rõ nghĩa, biến nào có khả năng ít null nhất sẽ để trước) */

        List<Bank> bankList = banks.stream().filter(bank -> bankCode.equals(bank.getBankCode()))
                .collect(Collectors.toList());
        Bank bank = null;
        if (!bankList.isEmpty()) {
            bank = bankList.get(0);
        }
        return bank;
    }

}
