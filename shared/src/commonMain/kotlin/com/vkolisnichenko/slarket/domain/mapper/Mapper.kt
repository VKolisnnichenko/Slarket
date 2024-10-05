package com.vkolisnichenko.slarket.domain.mapper

interface Mapper<In, Out> {
    fun map(input: In): Out
}