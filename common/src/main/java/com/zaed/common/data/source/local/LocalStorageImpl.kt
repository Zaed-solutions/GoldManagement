package com.zaed.common.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zaed.common.data.model.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_storage")
val UserApprovementStatus = booleanPreferencesKey("user_approvement_status")
val UserId = stringPreferencesKey("user_id")

class LocalStorageImpl (
    private val context: Context
): LocalStorage {
    override fun getLocalUser(): Flow<Result<LocalUser>> {
        return  context.dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                Log.d("getUserApprovementStatus",exception.message.toString())
                Result.failure<Preferences>(exception)
            }
            .map { preferences ->
                val result = preferences[UserApprovementStatus] ?: false
                val userId = preferences[UserId] ?: ""
                Result.success(LocalUser(
                    userId = userId
                ))
            }
    }

    override suspend fun setLocalUser(value: LocalUser) {
        context.dataStore.edit { preferences ->
            preferences[UserId] = value.userId
        }
    }

}