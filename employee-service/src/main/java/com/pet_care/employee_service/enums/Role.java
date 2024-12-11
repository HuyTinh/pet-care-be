package com.pet_care.employee_service.enums;

/**
 * Enum representing the role of an employee in the system.
 * This enum is used to define the various roles that an employee can have within the organization.
 * It provides a set of predefined roles that are important for managing employee permissions and responsibilities.
 */
public enum Role {

    // Represents the hospital administrator, typically with the highest level of control and management
    HOSPITAL_ADMINISTRATOR,

    // Represents a receptionist role, responsible for patient interactions and front desk duties
    RECEPTIONIST,

    // Represents a doctor, who provides medical care and consultation
    DOCTOR,

    // Represents a pharmacist, responsible for dispensing medications and advising patients
    PHARMACIST,

    // Represents a warehouse manager, responsible for managing the inventory of medical supplies and products
    WAREHOUSE_MANAGE
}
