package com.petcare.service.impl;

import com.petcare.entity.Account;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.AccountRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountImpl implements EntityService<Account, Long> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllEntity() {

        Iterable<Account> accounts = accountRepository.findAll();
        List<Account> accountList = ArrayMapper.mapperIterableToList(accounts);

        return accountList;
    }

    @Override
    public Optional<Account> getEntityById(Long id) {

        Optional<Account> account = accountRepository.findById(id);

        return account;
    }

    @Override
    public Account createEntity(Account account) {

        Optional<Account> savedAccount = getEntityById(account.getId());

        if (savedAccount.isPresent()) {
            throw new APIException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }

        return accountRepository.save(account);
    }

    @Override
    public Account updateEntity(Account account) {

        Account savedAccount = getEntityById(account.getId())
                .orElseThrow(() -> new APIException(ErrorCode.ACCOUNT_NOT_FOUND));

        return accountRepository.save(account);
    }

    @Override
    public void deleteEntity(Long id) {

        Account account = getEntityById(id)
                .orElseThrow(() -> new APIException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountRepository.delete(account);

    }
}
