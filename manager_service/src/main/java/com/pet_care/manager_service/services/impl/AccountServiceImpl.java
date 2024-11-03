package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;
import com.pet_care.manager_service.dto.response.EmployeeResponse;
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<AccountResponse> getAllEmployee() {
//        List<Object[]> listEmployee = accountRepository.getAllEmployee();
//        if (listEmployee.isEmpty()) {
//            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
//        };
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        List<Object> redisObjects = redisTemplate.opsForHash().values(HASH_KEY);

        // loc. va` ep' thành Object[]
        List<Object[]> listEmployee = redisObjects.stream()
                .filter(obj -> obj instanceof Object[]) // Ensure the object is an Object[]
                .map(obj -> (Object[]) obj) // Cast to Object[]
                .collect(Collectors.toList());
//        Clear Data Redis

        System.out.println(">> Check List Employee True : " + listEmployee);
        if (listEmployee.isEmpty()) {
            listEmployee = accountRepository.getAllEmployee();
            System.out.println(">> Check List Employee True : " + listEmployee);

            if (listEmployee.isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
            }
            for(Object[] objects : listEmployee) {
                redisTemplate.opsForHash().put(HASH_KEY, objects[0].toString(), objects);
            }
        }
        System.out.println(">> Check List Employee True : " + listEmployee);
        listEmployee.sort(Comparator.comparing(objects -> (Comparable) objects[0]));

        return listEmployee.stream()
                .map(this::employeeResponse)
                .map(this::convertToAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> getAllEmployeeTrue() {
//        List<Object[]> listEmployee = accountRepository.getAllEmployeeTrue();
//        if (listEmployee.isEmpty()) {
//            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
//        };
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        List<Object> redisObjects = redisTemplate.opsForHash().values(HASH_KEY);

        // loc. va` ep' thành Object[]
        List<Object[]> listEmployeeTrue = redisObjects.stream()
                .filter(obj -> obj instanceof Object[]) // Ensure the object is an Object[]
                .map(obj -> (Object[]) obj) // Cast to Object[]
                .collect(Collectors.toList());

        System.out.println(">> Check List Employee True : " + listEmployeeTrue);
        if (listEmployeeTrue.isEmpty()) {
            listEmployeeTrue = accountRepository.getAllEmployeeTrue();
            System.out.println(">> Check List Employee True : " + listEmployeeTrue);

            if (listEmployeeTrue.isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
            }
            for(Object[] objects : listEmployeeTrue) {
                redisTemplate.opsForHash().put(HASH_KEY, objects[0].toString(), objects);
            }
        }
        System.out.println(">> Check List Employee True : " + listEmployeeTrue);
        listEmployeeTrue.sort(Comparator.comparing(objects -> (Comparable) objects[0]));

        return listEmployeeTrue.stream()
                .map(this::employeeResponse)
                .map(this::convertToAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> getAllByRole(Long id) {
//        List<Object[]> listEmployeeByRole = accountRepository.getAllByRole(id);
//
//        if (listEmployeeByRole.isEmpty()) {
//            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
//        };
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        List<Object> redisObjects = redisTemplate.opsForHash().values(HASH_KEY);

        // loc. va` ep' thành Object[]
        List<Object[]> listEmployeeByRole = redisObjects.stream()
                .filter(obj -> obj instanceof Object[]) // Ensure the object is an Object[]
                .map(obj -> (Object[]) obj) // Cast to Object[]
                .collect(Collectors.toList());

        System.out.println(">> Check List Employee True : " + listEmployeeByRole);
        if (listEmployeeByRole.isEmpty()) {
            listEmployeeByRole = accountRepository.getAllByRole(id);
            System.out.println(">> Check List Employee True : " + listEmployeeByRole);

            if (listEmployeeByRole.isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
            }
            for(Object[] objects : listEmployeeByRole) {
                redisTemplate.opsForHash().put(HASH_KEY, objects[0].toString(), objects);
            }
        }
        System.out.println(">> Check List Employee True : " + listEmployeeByRole);
        listEmployeeByRole.sort(Comparator.comparing(objects -> (Comparable) objects[0]));

        return listEmployeeByRole.stream()
                .map(this::employeeResponse)
                .map(this::convertToAccountResponse)
                .collect(Collectors.toList());
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

}
