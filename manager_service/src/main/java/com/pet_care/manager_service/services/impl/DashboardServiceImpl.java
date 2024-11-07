package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.*;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.DashboardService;
import io.swagger.v3.oas.models.links.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    PrescriptionDetailRepository predRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;
    @Autowired
    private ServicesRepository servicesRepository;

    public CustomerHomeDashboardResponse getCountCustomer(LocalDate date){
        List<Object[]> listCustomer = invoiceRepository.getAllCustomerToday(date);
        Long count = (long) listCustomer.size();
        System.out.println("Check list customer: " + count);
        return CustomerHomeDashboardResponse.builder()
                .countCustomer(count)
                .build();
    }

    public AppointmentHomeDashboardResponse getCountAppointment(LocalDate date){

        List<Appointment> listAppointment = invoiceRepository.findByAppointmentDate(date);
        Long count = (long) listAppointment.size();
        System.out.println("Check list appointment: " + count);
        return AppointmentHomeDashboardResponse.builder()
                .countAppointment(count)
                .build();
    }

    public InvoiceHomeDashboardResponse getCountInvoice(LocalDate date ){
//        LocalDate now = LocalDate.now();
        List<Object[]> listInvoice = invoiceRepository.getAllInvoiceToday(date);
        Long count = (long) listInvoice.size();
        System.out.println("Check list invoice: " + count);
        return InvoiceHomeDashboardResponse.builder()
                .countInvoice(count)
                .build();
    }


    public CustomerPrescriptionResponse getCustomerPrescription(Long prescriptionId){
        Prescription presc = prescriptionRepository.findById(prescriptionId).orElse(null);
        Pet pet = petRepository.findById(presc.getPet().getId()).get();
        Customer customer = customerRepository.findById(pet.getId()).get();
        if(customer == null){
            throw new AppException(ErrorCode.CUSTOMER_NOTFOUND);
        }
        return CustomerPrescriptionResponse.builder()
                .id(customer.getId())
                .first_name(customer.getFirst_name())
                .last_name(customer.getLast_name())
                .phone_number(customer.getPhone_number())
                .build();
    }
    public PrescriptionHomeDashboardResponse prescriptionHomeDashboardResponse(Long id){
        Set<PrescriptionDetailResponse> prescriptionDetailResponseSet = new HashSet<>();
        Prescription prescription = prescriptionRepository.findById(id).get();
        List<Prescription_Details> prescription_details = predRepository.findByPrescription(prescription);
        prescriptionDetailResponseSet = prescription_details.stream()
                    .map(this::convertPrescriptionDetailResponse)
                    .collect(Collectors.toSet());

        Profile profile = prescription.getProfile();
        ProfilesDoctorResponse profilesResponse = ProfilesDoctorResponse.builder()
                .id(profile.getId())
                .first_name(profile.getFirst_name())
                .last_name(profile.getLast_name())
                .build();
        return PrescriptionHomeDashboardResponse.builder()
                .prescriptionId(id)
                .disease_name(prescription.getDisease_name())
                .customerPrescription(getCustomerPrescription(id))
                .profilesDoctor(profilesResponse)
                .petPrescription(convertPetPrescriptionResponse(prescription))
                .prescriptionDetails(prescriptionDetailResponseSet)
                .build();
    }
    public PrescriptionDetailResponse convertPrescriptionDetailResponse(Prescription_Details prescription_details){
        Medicine medicine = medicineRepository.findById(prescription_details.getMedicine().getId()).get();
        MedicineResponse medicineResponse =  MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .build();
        return PrescriptionDetailResponse.builder()
                .id(prescription_details.getId())
                .quantity(prescription_details.getQuantity())
                .medicineResponse(medicineResponse)
                .description(prescription_details.getNote())
                .build();
    }

    public PetPrescriptionResponse convertPetPrescriptionResponse(Prescription prescription){
        Pet pet = petRepository.findById(prescription.getPet().getId()).get();
        Species species = pet.getSpecies();
        SpeciesResponse speciesResponse = SpeciesResponse.builder()
                .id(species.getId())
                .name(species.getName())
                .build();
        return PetPrescriptionResponse.builder()
                .id(prescription.getId())
                .name(pet.getName())
                .weight(pet.getWeight())
                .age(pet.getAge())
                .speciesResponse(speciesResponse)
                .build();
    }
    public Set<PrescriptionHomeDashboardResponse> getPrescriptionHomeDashboard(){
        LocalDate now = LocalDate.now();
        List<Object[]> listPres = prescriptionRepository.getPrescriptionToday(now);
        Set<PrescriptionHomeDashboardResponse> prescriptionHomeDashboardResponseSet = new HashSet<>();
        for(Object[] objects : listPres){
            Long predId = (Long) objects[0];
            prescriptionHomeDashboardResponseSet.add(prescriptionHomeDashboardResponse(predId));
        }
        Set<PrescriptionHomeDashboardResponse> sortPrescriptionHomeDashboardResponseSet = prescriptionHomeDashboardResponseSet
                .stream().sorted(Comparator.comparing(PrescriptionHomeDashboardResponse::getPrescriptionId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortPrescriptionHomeDashboardResponseSet;
    }

    @Override
    public DashboardResponse getDashboardHome() {
        LocalDate now = LocalDate.now();

        return DashboardResponse.builder()
                .customers(getCountCustomer(now))
                .appointments(getCountAppointment(now))
                .invoices(getCountInvoice(now))
                .prescriptions(getPrescriptionHomeDashboard())
                .build();
    }

    @Override
    public Set<AppointmentHomeDashboardTableResponse> listAppointmentHomeDashboard(){
        LocalDate now = LocalDate.now();
        List<Appointment> listApp = appointmentRepository.getAllAppointmentToDay(now);
        Set<AppointmentHomeDashboardTableResponse> listAppointment = new HashSet<>();
        for(Appointment appointment : listApp){
            listAppointment.add(appointmentHomeDashboardTable(appointment));
        }
        Set<AppointmentHomeDashboardTableResponse> sordAppointment = listAppointment.stream()
                .sorted(Comparator.comparing(AppointmentHomeDashboardTableResponse::getAppointmentId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sordAppointment;
    }

    public AppointmentHomeDashboardTableResponse appointmentHomeDashboardTable(Appointment appointment) {

        return AppointmentHomeDashboardTableResponse.builder()
                .appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointment_date())
                .appointmentTime(appointment.getAppointment_hour())
                .petResponses(petResponses(appointment))
                .customerPrescriptionResponse(customerPrescriptionResponse(appointment))
                .profilesDoctorResponse(profilesDoctorResponse(appointment))
                .appointmentStatus(appointment.getStatus_accept())
                .build();
    }

    public Set<PetResponse> petResponses(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        Set<PetResponse> petResponses = new HashSet<>();
        for(Pet pet : pets){
//            Pet p = petRepository.findById(pet.getId()).get();
            Species species = pet.getSpecies();
            SpeciesResponse speciesResponse =  SpeciesResponse.builder()
                    .id(pet.getId())
                    .name(species.getName())
                    .build();
            petResponses.add(
                    PetResponse.builder()
                        .id(pet.getId())
                        .name(pet.getName())
                        .weight(pet.getWeight())
                        .age(pet.getAge())
                        .speciesResponse(speciesResponse)
                        .prescriptionResponses(prescriptionByPetId(pet.getId()))
                        .build()
            );
        }
        Set<PetResponse> sortPet = petResponses.stream()
                .sorted(Comparator.comparing(PetResponse::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortPet;
    }

    public CustomerPrescriptionResponse customerPrescriptionResponse(Appointment appointment){
        return CustomerPrescriptionResponse.builder()
                .id(appointment.getCustomer().getId())
                .first_name(appointment.getCustomer().getFirst_name())
                .last_name(appointment.getCustomer().getLast_name())
                .phone_number(appointment.getCustomer().getPhone_number())
                .build();
    }

    public ProfilesDoctorResponse profilesDoctorResponse(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        long profileId = 0;
        for(Pet pet : pets){
            Prescription prescription = pet.getPrescriptions();
            if(prescription == null){
                Profile profile = new Profile();
                System.out.println("Check profiles 1: " + profile);
                continue;
            }
            Profile profile = prescription.getProfile();
            profileId = profile.getId();
            System.out.println("Check profiles 2: " + profile);
        }
        Profile prf = new Profile();
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isPresent())
        {
            prf = profile.get();
        }
        System.out.println("Check profiles 3: " + profile);
        return ProfilesDoctorResponse.builder()
                .id(prf.getId())
                .first_name(prf.getFirst_name())
                .last_name(prf.getLast_name())
                .build();
    }

    public Set<PrescriptionResponse> prescriptionResponse(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        Set<PrescriptionDetailResponse> presriptionDetailResponses ;
        Set<PrescriptionResponse> prescriptionResponses = new HashSet<>();
        for (Pet pet : pets){
            List<Object[]> prescriptions = prescriptionRepository.getPrescriptionByPetId(pet.getId());
            for(Object[] obj : prescriptions){
                Prescription prescription = prescriptionRepository.findById((Long) obj[0]).get();
                List<Prescription_Details> prescription_details = prescriptionDetailRepository.findByPrescription(prescription);

                presriptionDetailResponses = prescription_details.stream()
                        .map(this::convertToPrescriptionDetailResponse)
                        .collect(Collectors.toSet());

                prescriptionResponses.add(PrescriptionResponse.builder()
                        .id((Long) obj[0])
                        .create_date((LocalDate) obj[1])
                        .note((String) obj[2])
                        .disease_name((String) obj[3])
                        .prescriptionDetailResponse(presriptionDetailResponses)
                        .build()
                );
            }
        }
        return prescriptionResponses;
    }

    public MedicineResponse convertToMedicineResponse(Medicine medicine){
        return MedicineResponse.builder()
                .id( medicine.getId())
                .name(medicine.getName())
                .build();
    }

    public PrescriptionDetailResponse convertToPrescriptionDetailResponse(Prescription_Details prescription_details){
        Medicine medicine = medicineRepository.findById(prescription_details.getMedicine().getId()).get();
        MedicineResponse medicineResponse = convertToMedicineResponse(medicine);
        return PrescriptionDetailResponse.builder()
                .id(prescription_details.getId())
                .quantity(prescription_details.getQuantity())
                .medicineResponse(medicineResponse)
                .build();
    }

    public Set<PrescriptionResponse> prescriptionByPetId(Long id) {

        List<Object[]> pres = prescriptionRepository.getPrescriptionByPetId(id);
        if (pres == null) {
            throw new AppException(ErrorCode.PRESCRIPTION_NOTFOUND);
        }
        Set<PrescriptionDetailResponse> presriptionDetailResponses;
        Set<PrescriptionResponse> prescriptionResponses = new HashSet<>();
        for (Object[] obj : pres) {
            Prescription prescription = prescriptionRepository.findById((Long) obj[0]).get();
            List<Prescription_Details> prescription_details = prescriptionDetailRepository.findByPrescription(prescription);
            System.out.println("Check prescription_details : " + prescription_details.size());
//
            presriptionDetailResponses = prescription_details.stream()
                    .map(this::convertToPrescriptionDetailResponse)
                    .collect(Collectors.toSet());

            prescriptionResponses.add(PrescriptionResponse.builder()
                    .id((Long) obj[0])
                    .create_date((LocalDate) obj[1])
                    .note((String) obj[2])
                    .disease_name((String) obj[3])
                    .prescriptionDetailResponse(presriptionDetailResponses)
                    .build()
            );
        }
        return prescriptionResponses;
    }


    public AppointmentChartTodayResponse appointmentChartTodayResponse(Object[] row){
        return AppointmentChartTodayResponse.builder()
                .hour_name((String) row[0])
                .appointment((Long) row[1])
                .build();
    }

    public List<AppointmentChartTodayResponse> listAppointmentChartTodayResponse(){
        LocalDate now = LocalDate.now();
        List<Object[]> listAppointment = appointmentRepository.getAppointmentHoursToday(now);
        return listAppointment.stream().map(this::appointmentChartTodayResponse).toList();
    }

    public AppointmentChartResponse appointmentChartResponse(Object[] row){
        return AppointmentChartResponse
                .builder()
                .hour_name((String) row[0])
                .appointment((Long) row[1])
                .percent((BigDecimal) row[2])
                .build();
    }

    public Set<AppointmentChartResponse> appointmentChartResponseSet(LocalDate from_date, LocalDate to_date){
        Set<Object[]> chartAppointment = appointmentRepository.getAppointmentChartFromAndToDate(from_date, to_date);
        Set<AppointmentChartResponse> appointmentChartResponses = new HashSet<>();
        appointmentChartResponses.addAll(chartAppointment.stream().map(this::appointmentChartResponse).collect(Collectors.toSet()));

        Set<AppointmentChartResponse> sortAppChartResponses = appointmentChartResponses.stream()
                .sorted(Comparator.comparing(AppointmentChartResponse::getHour_name))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortAppChartResponses;
    }

    public Set<AppointmentChartResponse> appointmentByMonthAndYear(Long month, Long year){
        Set<Object[]> chartAppointment = appointmentRepository.getAppointmentChartMonthAndYear(month, year);
        Set<AppointmentChartResponse> appointmentChartResponses = new HashSet<>();
        appointmentChartResponses.addAll(chartAppointment.stream().map(this::appointmentChartResponse).collect(Collectors.toSet()));

        Set<AppointmentChartResponse> sortAppointment = appointmentChartResponses.stream()
                .sorted(Comparator.comparing(AppointmentChartResponse::getHour_name))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortAppointment;
    }

    public Set<AppointmentYearFirstAndYearSecondResponse> appointmentYearFirstAndYearSecond(Long year_first,Long year_second){
        Set<Object[]> appYearFirstAndSecond = appointmentRepository.getAppointmentYearFirstAndYearSecond(year_first,year_second);
        Set<AppointmentYearFirstAndYearSecondResponse> appYFAndYS = new HashSet<>();
        appYFAndYS.addAll(appYearFirstAndSecond.stream().map(this::appYearFirstAndYearSecond).collect(Collectors.toSet()));

        Set<AppointmentYearFirstAndYearSecondResponse> sortAppointment = appYFAndYS.stream()
                .sorted(Comparator.comparing((AppointmentYearFirstAndYearSecondResponse::getMonth)))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortAppointment;
    }

    public AppointmentFromAndTodateResponse appFromAndTodateResponse(
            LocalDate from_date, LocalDate to_date,
            Long month, Long year,
            Long year_first, Long year_second
            ){
        LocalDate today = LocalDate.now();
        if(month == null && year == null){
            month = (long) today.getMonthValue();
            year = (long) today.getYear();
        }
        if(month == null ){
            month = (long) today.getMonthValue();
        }
        if(year == null){
            year = (long) today.getYear();
        }
        if(from_date == null && to_date == null){
            from_date = today.withDayOfMonth(1);
            to_date = today.withDayOfMonth(today.lengthOfMonth());
        }
        if(from_date != null && to_date == null){
            to_date = from_date.withDayOfMonth(from_date.lengthOfMonth());
        }
        if(from_date == null && to_date != null){
            from_date = to_date.withDayOfMonth(1);
        }
        if(year_first == null && year_second == null){
            year_second = (long) today.getYear();
            year_first = year_second - 1;
        }
        if(year_first == null && year_second != null){
            year_first = year_second - 1;
        }
        if(year_first != null && year_second == null){
            year_second = year_first + 1;
        }
        return AppointmentFromAndTodateResponse.builder()
                .from_date(from_date)
                .to_date(to_date)
                .appointment_chart_response(appointmentChartResponseSet(from_date, to_date))
                .month(month)
                .year(year)
                .appointment_month_year(appointmentByMonthAndYear(month, year))
                .year_first(year_first)
                .year_second(year_second)
                .appointment_year_first_year_second_response(appointmentYearFirstAndYearSecond(year_first,year_second))
                .build();
    }

    public AppointmentYearFirstAndYearSecondResponse appYearFirstAndYearSecond(Object[] row){
        return AppointmentYearFirstAndYearSecondResponse.builder()
                .month((Long) row[0])
                .monthName((String) row[1])
                .count_year_first((Long) row[2])
                .count_year_second((Long) row[3])
                .percent_year_first((BigDecimal) row[4])
                .percent_year_second((BigDecimal) row[5])
                .build();
    }

    public InvoiceOfYearResponse invoiceOfYearResponse(Object[] row){

        return InvoiceOfYearResponse.builder()
                .month((Long) row[0])
                .monthName((String) row[1])
                .total((Double) row[2])
                .build();
    }

    public InvoiceOfMonthResponse invoiceOfMonthResponse(Object[] row){
        return InvoiceOfMonthResponse.builder()
                .day((Integer) row[0])
                .total((Double) row[1])
                .build();
    }

    public Set<InvoiceOfMonthResponse> invoiceOfMonthSet(Long year, Long month){
        System.out.println("Check year: " + year);
        System.out.println("Check month: " + month);

        Set<Object[]> invoiceOfMonth = invoiceRepository.getRevenueOfMonthAnhYear( year,month);
        Set<InvoiceOfMonthResponse> iofMonth = new HashSet<>();
        iofMonth.addAll(invoiceOfMonth.stream().map(this::invoiceOfMonthResponse).collect(Collectors.toSet()));
        Set<InvoiceOfMonthResponse> sortedInvoice = iofMonth.stream()
                .sorted(Comparator.comparing(InvoiceOfMonthResponse::getDay))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortedInvoice;
    }

    public Set<InvoiceOfYearResponse> invoiceOfYearSet(Long year){
        Set<Object[]> invoiceOfYear = invoiceRepository.getRevenueYear(year);
        Set<InvoiceOfYearResponse> iofSet = new HashSet<>();
        iofSet.addAll(invoiceOfYear.stream().map(this::invoiceOfYearResponse).collect(Collectors.toSet()));

        Set<InvoiceOfYearResponse> sortInvoice = iofSet.stream()
                .sorted(Comparator.comparing((InvoiceOfYearResponse::getMonth)))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortInvoice;
    }

    public Double getTotalRevenueByDate(LocalDate date){
        List<Invoice> listInvoice = invoiceRepository.getTotalByDate(date);
        Double total = (double) 0;
        if(listInvoice.isEmpty()){
            return total;
        }
        for(Invoice invoice : listInvoice){
            total = total + invoice.getTotal();
        }
        return total;
    }
    public InvoiceCountResponse invoiceCountResponse(Long month, Long year, Boolean today,  Long year_first, Long year_second){
        LocalDate now = LocalDate.now();
        String today_yesterday  = "";
        if(today == true ){
            now = LocalDate.now();
            today_yesterday = "Today";
        }
        if(today == false ){
            now = now.minusDays(1);
            today_yesterday = "Yesterday";
        }
        if(year_first == null && year_second == null){
            year_second = (long) now.getYear();
            year_first = year_second - 1;
        }
        if(year_first == null && year_second != null){
            year_first = year_second - 1;
        }
        if(year_first != null && year_second == null){
            year_second = year_first + 1;
        }
        if(month == null){
           month = (long) now.getMonthValue();
            System.out.println("Check year: " + month);

        }
        if(year == null){
            year = (long) now.getYear();
            System.out.println("Check year: " + year);
        }
        return InvoiceCountResponse.builder()
                .appointments(getCountAppointment(now))
                .customers(getCountCustomer(now))
                .invoices(getCountInvoice(now))
                .total(getTotalRevenueByDate(now))
                .today_yesterday(today_yesterday)
                .month(month)
                .year(year)
                .invoiceOfMonth(invoiceOfMonthSet( year,month))
                .invoiceOfYears(invoiceOfYearSet(year))
                .year_first(year_first)
                .year_second(year_second)
                .revenueYearFirstAndYearSeconds(getRevenueYFAndYSs(year_first,year_second))
                .build();
    }

    public RevenueYearFirstAndYearSecondResponse getRevenueYearFirstAndYearSecond(Object[] row){

        return RevenueYearFirstAndYearSecondResponse.builder()
                .month((Long) row[0])
                .monthName((String) row[1])
                .revenue_year_first((Double) row[2])
                .revenue_year_second((Double) row[3])
                .build();
    }

    public Set<RevenueYearFirstAndYearSecondResponse> getRevenueYFAndYSs(Long year_first, Long year_second){
        Set<Object[]> revYFAndYS = invoiceRepository.getInvoiceYearFirstAndYearSecond(year_first,year_second);
        Set<RevenueYearFirstAndYearSecondResponse> revenueYearFirstAndYearSecondResponses = new HashSet<>();
        revenueYearFirstAndYearSecondResponses.addAll(revYFAndYS.stream().map(this::getRevenueYearFirstAndYearSecond).collect(Collectors.toSet()));

        Set<RevenueYearFirstAndYearSecondResponse> sortRevenue = revenueYearFirstAndYearSecondResponses.stream()
                .sorted(Comparator.comparing(RevenueYearFirstAndYearSecondResponse::getMonth))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortRevenue;
    }

    public ServiceOfMonthResponse serviceOfMonth (Object[] row){
        return ServiceOfMonthResponse.builder()
                .id((Long) row[0])
                .name((String) row[1])
                .count((Long) row[2])
                .build();
    }

    public Set<ServiceOfMonthResponse> serviceOfMonthSortServiceCounts(Long month, Long year){
        Set<Object[]> somResponses = servicesRepository.getTop10ServiceOfMonthSortCount(month, year);
        Set<ServiceOfMonthResponse> somResponseSet = new HashSet<>();
        somResponseSet.addAll(somResponses.stream().map(this::serviceOfMonth).collect(Collectors.toSet()));

        Set<ServiceOfMonthResponse> sortServiceCount = somResponseSet.stream()
                .sorted(Comparator.comparing(ServiceOfMonthResponse::getCount).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortServiceCount;
    }

    public ServiceRevenueOfMonthResponse serviceRevenueOfMonth (Object[] row){
        return ServiceRevenueOfMonthResponse.builder()
                .id((Long) row[0])
                .name((String) row[1])
                .total((Double) row[3])
                .percent_revenue((Double) row[4])
                .build();
    }

    public Set<ServiceRevenueOfMonthResponse> serviceRevenueOfMonthSortServiceCounts(Long month, Long year){
        Set<Object[]> somResponses = servicesRepository.getTop10ServiceOfMonthSortCount(month, year);
        Set<ServiceRevenueOfMonthResponse> somResponseSet = new HashSet<>();
        somResponseSet.addAll(somResponses.stream().map(this::serviceRevenueOfMonth).collect(Collectors.toSet()));

        Set<ServiceRevenueOfMonthResponse> sortServiceCount = somResponseSet.stream()
                .sorted(Comparator.comparing(ServiceRevenueOfMonthResponse::getTotal).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortServiceCount;
    }

    public ServiceYearFirstAndYearSecondResponse serviceYearFirstAndYearSecond(Object[] row){
        return ServiceYearFirstAndYearSecondResponse.builder()
                .month((Long) row[0])
                .monthName((String) row[1])
                .service_year_first((Long) row[2])
                .service_year_second((Long) row[3])
                .percent_year_first((BigDecimal) row[4])
                .percent_year_second((BigDecimal) row[5])
                .build();
    }

    public Set<ServiceYearFirstAndYearSecondResponse> serviceYearFirstAndYearSecondResponseSet(Long year_first, Long year_second){
        Set<Object[]> serviceYFAYS = servicesRepository.getServiceYearFirstAndYearSecond(year_first,year_second);
        Set<ServiceYearFirstAndYearSecondResponse> serviceYearFirstAndYearSecond = new HashSet<>();
        serviceYearFirstAndYearSecond.addAll(serviceYFAYS.stream().map(this::serviceYearFirstAndYearSecond).collect(Collectors.toSet()));
        Set<ServiceYearFirstAndYearSecondResponse> sortService = serviceYearFirstAndYearSecond.stream()
                .sorted(Comparator.comparing(ServiceYearFirstAndYearSecondResponse::getMonth))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortService;
    }

    public ServiceChartResponse getServiceChart(Long month, Long year, Long year_first, Long year_second){
        LocalDate now = LocalDate.now();
        if(month == null){
            month = (long) now.getMonthValue();
        }
        if(year == null){
            year = (long) now.getYear();
        }
        if(year_first == null && year_second == null){
            year_second = (long) now.getYear();
            year_first = year_second - 1;
        }
        if(year_first != null && year_second == null){
            year_second = year_first + 1;
        }
        if(year_second != null && year_first == null){
            year_first = year_second - 1;
        }

        return ServiceChartResponse.builder()
                .month(month)
                .year(year)
                .serviceOfMonth(serviceOfMonthSortServiceCounts(month,year))
                .serviceRevenueOfMonth(serviceRevenueOfMonthSortServiceCounts(month,year))
                .year_first(year_first)
                .year_second(year_second)
                .serviceYearFirstAndYearSecond(serviceYearFirstAndYearSecondResponseSet(year_first,year_second))
                .build();
    }
}
