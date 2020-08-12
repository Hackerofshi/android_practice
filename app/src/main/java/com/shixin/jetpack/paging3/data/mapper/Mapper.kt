package com.shixin.jetpack.paging3.data.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}