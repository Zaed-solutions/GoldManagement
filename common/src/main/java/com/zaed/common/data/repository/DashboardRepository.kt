package com.zaed.common.data.repository

import com.zaed.common.data.model.dashboard.Dashboard

interface DashboardRepository {
    suspend fun getDashboardData(): Result<Dashboard>
}