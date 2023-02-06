package com.jaehong.data.mapper

import com.jaehong.data.local.database.entity.RecentEntity
import com.jaehong.domain.model.RecentInfo

object Mapper {

    fun RecentEntity.dataToDomain(): RecentInfo{
        return RecentInfo(this.keyword,this.saveTime)
    }

    fun RecentInfo.domainToData(): RecentEntity {
        return RecentEntity(this.keyword,this.saveTime)
    }
}