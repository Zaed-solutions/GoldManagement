package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.data.model.request.DeleteUserRequest
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.data.model.request.UpdateUserRequest
import com.zaed.common.ui.auth.login.LoginError
import com.zaed.common.ui.auth.login.log
import com.zaed.common.ui.auth.signup.SignUpError
import com.zaed.common.ui.auth.signup.log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthenticationRemoteSourceImpl(
    firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : AuthenticationRemoteSource {
    private val USERS_COLLECTION = "users"
    private val usersCollection = firestore.collection(USERS_COLLECTION)


    override suspend fun loginUser(request: LoginUserRequest): Result<User> {
        return try {
            val userQuery = usersCollection.whereEqualTo("userName", request.userName).get().await()
            if (userQuery.documents.isEmpty()) {
                return Result.failure(LoginError.UserNotFound())
            }
            val user = userQuery.documents[0].toObject(User::class.java)
            if (user == null) {
                return Result.failure(LoginError.UserNotFound())
            }
            if (user.password != request.password) {
                return Result.failure(LoginError.InCorrectPassword())
            }
            Result.success(user)
        } catch (e: Exception) {
            val error = LoginError.LoginFailed(
                reason = e.message ?: "Unknown error",
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".loginUser.catch"
            )
            crashlytics.recordException(error)
            crashlytics.log(error.toString())
            error.log()
            Result.failure(e)
        }
    }


    override fun fetchCurrentUser(userId: String): Flow<Result<User>> = callbackFlow {
        try {
            usersCollection.document(userId).addSnapshotListener { value, e ->
                if (e != null) {
                    val error = LoginError.LoginFailed(
                        reason = e.message.toString(),
                        location = AuthenticationRemoteSourceImpl::class.simpleName + ".fetchCurrentUser.While fetching user"
                    )
                    crashlytics.recordException(error)
                    crashlytics.log(error.toString())
                    error.log()
                    trySend(Result.failure(error))
                } else {
                    if (value != null && value.exists()) {
                        val user = value.toObject(User::class.java)
                        trySend(Result.success(user!!))
                    } else {
                        val error = LoginError.UserNotFound()
                        crashlytics.recordException(error)
                        crashlytics.log(error.toString())
                        trySend(Result.failure(error))
                    }
                }
            }

        } catch (e: Exception) {
            val error = LoginError.LoginFailed(
                reason = e.message.toString(),
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".fetchCurrentUser.catch"
            )
            crashlytics.recordException(error)
            crashlytics.log(error.toString())
            error.log()
            trySend(Result.failure(error))
        }
        awaitClose { }

    }

    override fun fetchUsers(): Flow<Result<List<User>>> = callbackFlow {
        try {
            usersCollection.addSnapshotListener { value, e ->
                if (e != null) {
                    crashlytics.recordException(e)
                    trySend(Result.failure(e))
                } else {
                    val users = value?.toObjects(User::class.java)?: emptyList()
                    trySend(Result.success(users))
                }
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        } finally {
            awaitClose { }
        }
    }

    override suspend fun updateUser(request: UpdateUserRequest): Result<Unit> {
        return try {
            usersCollection.document(request.userId).update(request.updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(request: DeleteUserRequest): Result<Unit> {
        return try{
            usersCollection.document(request.userId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun signUpUser(request: SignUpUserRequest): Result<User> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("userName", request.userName)
                .get(Source.SERVER)
                .await()

            if (!querySnapshot.isEmpty) {
                Result.failure(SignUpError.UserAlreadyExists())
            } else {
                val document = usersCollection.document()
                val user = User(
                    id = document.id,
                    fullName = request.fullName,
                    userName = request.userName,
                    password = request.password,
                    approvementStatusType = UserApprovementStatusType.PENDING,
                    role = request.role
                )

                document.set(user).await()
                Result.success(user)
            }
        } catch (e: Exception) {
            val error = SignUpError.SignUpFailed(
                reason = e.message ?: "Unknown error",
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".signUpUser.catch"
            )
            crashlytics.recordException(error)
            crashlytics.log(error.toString())
            error.log()
            Result.failure(error)
        }
    }


}