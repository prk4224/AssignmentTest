package com.jaehong.domain.model

enum class UiStateResult(val message: String){
    SUCCESS("성공"),
    LOADING("로딩 중"),
    ERROR("실패")
}