package com.shixin.ui.jetpack.paging3.data.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}