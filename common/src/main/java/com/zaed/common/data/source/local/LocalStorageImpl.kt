package com.zaed.common.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zaed.common.data.model.authentication.LocalUser
import com.zaed.common.ui.util.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_storage")
val UserApprovalStatus = booleanPreferencesKey("user_approvement_status")
val UserId = stringPreferencesKey("user_id")
val appLanguageCode = stringPreferencesKey("app_language_code")

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
                val result = preferences[UserApprovalStatus] ?: false
                val userId = preferences[UserId] ?: ""
                Result.success(
                    LocalUser(
                    userId = userId
                )
                )
            }
    }

    override suspend fun setLocalUser(value: LocalUser) {
        context.dataStore.edit { preferences ->
            preferences[UserId] = value.userId
        }
    }

    override fun fetchAppLanguage(): Flow<Result<AppLanguage>> {
        return context.dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                Log.d("fetchAppLanguage",exception.message.toString())
                Result.failure<Preferences>(exception)
            }
            .map { preferences ->
                val result = preferences[appLanguageCode] ?: "en"
                val language = AppLanguage.fromCode(result)
                Result.success(
                    language
                )
            }
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage): Result<Unit> {
        return try{
            context.dataStore.edit { preferences ->
                preferences[appLanguageCode] = appLanguage.code
            }
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

}