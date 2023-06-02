package com.example.e_ngkringan.modeldata class AngkringanResponse(    val status: String,    val data: Resto)data class Resto(    val resto: List<AngkringanDetail>)data class ResponseDetail(    val status: String,    val data: AngkringanDetail)data class AngkringanDetail(    val id: Int,    val code: String,    val name: String,    val address: String,    val desc: String,    val image: String,    val total_table: Int,    val total_reserved: String,    val menu: List<DetailMenu>)data class ResponseMenu(    val status: String,    val data: List<DetailMenu>)data class DetailMenu(    val id: Int,    val name: String,    val price: Int,    val resto_code: String,    val image: String)data class SearchResponse(    val status: String,    val data: List<AngkringanDetail>)data class SearchMenu(    val status: String,    val data: List<DetailMenu>)data class BookingResponse(    val status: String,    val data: List<DetailBooking>)data class DetailBooking(    val dates: String,    val resto_code: String,    val note: String)data class ResponseListBooking(    val status: String,    val data: List<ListBooking>)data class ListBooking(    val id: Int,    val dates: String,    val user_code: String,    val resto_code: String,    val note: String,    val createdAt: String,    val updatedAt: String)data class DetailBookingResponse(    val status: String,    val data: ListBookings)data class ListBookings(    val id: Int,    val dates: String,    val user_code: String,    val resto_code: String,    val note: String,    val createdAt: String,    val updatedAt: String)