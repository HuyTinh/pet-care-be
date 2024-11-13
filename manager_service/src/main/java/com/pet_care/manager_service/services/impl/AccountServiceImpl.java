package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;
import com.pet_care.manager_service.dto.response.EmployeeResponse;
import com.pet_care.manager_service.dto.response.PageableResponse;
import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.entity.Profile;
import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.enums.RoleEnum;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.mapper.AccountMapper;
import com.pet_care.manager_service.repositories.AccountRepository;
import com.pet_care.manager_service.repositories.ProfileRepository;
import com.pet_care.manager_service.repositories.RoleRepository;
import com.pet_care.manager_service.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountMapper accountMapper;

    public static final String HASH_KEY = "account";

    public final RedisTemplate<String, Object> redisTemplate;

    public Optional<Role> getRoleByName(RoleEnum roleEnum) {
        return roleRepository.findByName(roleEnum);
    }

//    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        try {
            // Tạo account từ request
            Account account = Account.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .status(true)
                    .build();
            Account savedAccount = accountRepository.save(account);
            System.out.println(">> Check Account : " + account);
            // Select Role
            Role role = getRoleByName(request.getRoleName())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOTFOUND)); // Sử dụng RoleEnum

            // Tạo profile từ request
            Profile profile = Profile.builder()
                    .first_name(request.getProfile().getFirst_name())
                    .last_name(request.getProfile().getLast_name())
                    .account(savedAccount)
                    .email(request.getEmail())
                    .role(role)
                    .status(true)
                    .build();
            profileRepository.save(profile);
            savedAccount.setProfile(profile);
//          Save Redis
            Long id = savedAccount.getId();
            redisTemplate.opsForHash().put(HASH_KEY, id.toString(), accountMapper.toAccountResponse(savedAccount));
            return accountMapper.toAccountResponse(savedAccount);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    @Override
    public AccountResponse getAccountResponse(Long id) {

        Object[] redisObject ;
        redisObject = (Object[]) redisTemplate.opsForHash().get(HASH_KEY, id.toString());
//        AccountResponse account =  convertToAccountResponse(employeeResponse(redisObject));
        AccountResponse account = new AccountResponse();
        if(redisObject == null) {

                Account acc = accountRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOTFOUND));
                if (acc != null) {
                    redisTemplate.opsForHash().put(HASH_KEY, id.toString(), accountMapper.toAccountResponse(acc));
                }
                System.out.println("Check Account : " + acc);

            System.out.println("Check Account : " + account);
        }
        account = convertToAccountResponse(employeeResponse(redisObject));


        return account;
    }

//    @Override
    public AccountResponse deleteAccount(Long id) {

        Object[] redisObject ;
        redisObject = (Object[]) redisTemplate.opsForHash().get(HASH_KEY, id.toString());
//        AccountResponse account =  convertToAccountResponse(employeeResponse(redisObject));
        AccountResponse account = new AccountResponse();
        if(redisObject == null) {
            Account acc = accountRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOTFOUND));
            if (acc != null) {
                acc.setStatus(false);
                accountRepository.save(acc);
                account = accountMapper.toAccountResponse(acc);
                redisTemplate.opsForHash().put(HASH_KEY, id.toString(), account);
            }
        }
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOTFOUND));
        if (acc != null) {
            acc.setStatus(false);
            accountRepository.save(acc);
            account = accountMapper.toAccountResponse(acc);

        }

        System.out.println(account.getStatus());
        redisTemplate.opsForHash().put(HASH_KEY, id.toString(), account);
        return account;
    }


//    @Override
    public PageableResponse<AccountResponse> getAllEmployeeTrue(
            String search_query,
            int page_number, int page_size
    ) {

        redisTemplate.getConnectionFactory().getConnection().flushAll();
//        List<Object> redisObjects = redisTemplate.opsForHash().values(HASH_KEY);
        // loc. va` ep' thành Object[]
        Pageable pageable = PageRequest.of(page_number,page_size);
        Page<Account> accountPage = accountRepository.getAllEmployeeTrue(search_query,pageable);
        List<AccountResponse> accountResponseList = accountPage.getContent()
                .stream()
                .map(this::convertToAccountResponse)
                .toList();
        PageableResponse<AccountResponse> pageableResponse =  PageableResponse.<AccountResponse>builder()
                .content(accountResponseList)
                .pageNumber(accountPage.getNumber())
                .pageSize(accountPage.getSize())
                .totalPages(accountPage.getTotalPages())
                .build();
        return pageableResponse;
    }

//    @Override
    public PageableResponse<AccountResponse> getAllByRole(
            RoleEnum role_name,
            int page_number, int page_size
    ) {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        Pageable pageable = PageRequest.of(page_number,page_size);
        Page<Account> pageAccount = accountRepository.getAllByRole(role_name,pageable);
        List<AccountResponse> listAccount = pageAccount.getContent()
                .stream()
                .map(this::convertToAccountResponse)
                .toList();
        PageableResponse<AccountResponse> pageableResponse = PageableResponse.<AccountResponse>builder()
                .content(listAccount)
                .pageNumber(pageAccount.getNumber())
                .pageSize(pageAccount.getSize())
                .totalPages(pageAccount.getTotalPages())
                .build();

        return pageableResponse;
    }

    public EmployeeResponse employeeResponse(Object[] row){
        return EmployeeResponse.builder()
                .id((Long) row[0])
                .email((String) row[1])
                .password((String) row[2])
                .status((Boolean) row[3])
                .build();
    }

    public AccountResponse convertToAccountResponse(EmployeeResponse employeeResponse){
        Account account = accountRepository.findById(employeeResponse.getId())
                .orElseThrow( () -> new AppException(ErrorCode.ACCOUNT_NOTFOUND));
        Profile profile = profileRepository.findByAccount(account);
        return AccountResponse.builder()
                .id(employeeResponse.getId())
                .email(employeeResponse.getEmail())
                .password(employeeResponse.getPassword())
                .status(employeeResponse.isStatus())
                .profile(profile)
                .build();
    }

    public AccountResponse convertToAccountResponse(Account account){

        Profile profile = profileRepository.findByAccount(account);
        return AccountResponse.builder()
                .id(profile.getId())
                .email(account.getEmail())
                .password(account.getPassword())
                .status(account.isStatus())
                .profile(profile)
                .build();
    }

}
