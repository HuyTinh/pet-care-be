package com.pet_care.model

import jakarta.persistence.*

@Entity
@Table(name = "invoices")
class Invoice {
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Id
    var id: Long? = 0
}
