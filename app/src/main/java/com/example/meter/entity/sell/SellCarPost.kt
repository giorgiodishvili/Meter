package com.example.meter.entity.sell

import com.example.meter.entity.user.User
import com.google.gson.annotations.SerializedName

data class SellCarPost(
    val AUX: Boolean,
    val address: String,
    val air_conditioner: Boolean,
    val airbag: Int,
    val backuptire: Boolean,
    val bluetooth: Boolean,
    val board_computer: Boolean,
    val car_color: String,
    val central_lock: Boolean,
    val climate_control: Boolean,
    val cylinder: Int,
    val description: String,
    val disks: Boolean,
    val door_amount: String,
    val el_window: Boolean,
    val engine: Int,
    val fuel_type: String,
    val id: Int,
    val interior_color: String,
    val interior_make: String,
    val is_seat_head: Boolean,
    val manufacturer: String,
    val mileage: Int,
    val model: String,
    val multiwheel: Boolean,
    val navigation: Boolean,
    val photoUrl: List<String>,
    val price: Int,
    val rear_view_camera: Boolean,
    val releaseYear: String,
    val signalization: Boolean,
    val start_stop: Boolean,
    val tire_amount: String,
    val transmission_type: String,
    val user: User,
    val vin: String,
    val wheel_side: String,
    @SerializedName("კრუიზ_კონტროლი")
    val cruizeControl: Boolean,

    @SerializedName("ლუქი")
    val luqi: Boolean,

    @SerializedName("მოცურების_საწინააღმდეგო")
    val antiSlide: Boolean,

    @SerializedName("სავარძლის_მეხსიერება")
    val seatMemory: Boolean,

    @SerializedName("სისლე_ფარები")
    val antiFogLights: Boolean,

    @SerializedName("ტექ_დათვალიერება")
    val techInspection: Boolean,

    @SerializedName("ჰიდრავლიკა")
    val hyravlick: Boolean
)